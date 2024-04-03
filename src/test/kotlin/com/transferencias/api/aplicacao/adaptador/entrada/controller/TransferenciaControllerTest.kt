package com.transferencias.api.aplicacao.adaptador.entrada.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.transferencias.api.aplicacao.adaptador.saida.apivalidadora.api.ApiValidadora
import com.transferencias.api.aplicacao.adaptador.saida.apivalidadora.dto.ApiValidadoraResponse
import com.transferencias.api.aplicacao.adaptador.saida.email.api.EnvioDeEmail
import com.transferencias.api.aplicacao.adaptador.saida.email.servico.ApiParaEnvioDeEmail
import com.transferencias.api.aplicacao.dominio.repositorio.UsuarioRepository
import com.transferencias.api.aplicacao.servico.OrquestradorDeTransferencias
import com.transferencias.api.aplicacao.servico.auxiliares.PreparadorDeDadosDosUsuarios
import com.transferencias.api.helpers.Dummy
import com.transferencias.api.helpers.Dummy.criaDadosIniciaisDaTransacao
import com.transferencias.api.helpers.generateToken
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.client.HttpServerErrorException.InternalServerError
import java.math.BigDecimal
import java.math.RoundingMode

class TransferenciaControllerTest {
    @Nested
    @ExtendWith(SpringExtension::class)
    @SpringBootTest
    @AutoConfigureMockMvc
    @ActiveProfiles("test")
    inner class TestesIntegrados {

        @Autowired
        private lateinit var mockMvc: MockMvc

        @Autowired
        private lateinit var usuarioRepository: UsuarioRepository

        @Autowired
        private lateinit var objectMapper: ObjectMapper

        @MockBean
        private lateinit var enviaEmail: EnvioDeEmail

        @MockBean
        private lateinit var validaTransferencia: ApiValidadora

        val tokenUsuarioComum = generateToken(1)
        val tokenUsuarioLojista = generateToken(3)

        @Test
        fun `deve realizar a transferencia quando o usuario eh comum e possui saldo e a apiValidadora retorna true`() {
            `when`(validaTransferencia.executa()).thenReturn(ApiValidadoraResponse(true))
            val transferenciaRequest = Dummy.criaTransferenciaRequest(beneficiarioId = 2)

            mockMvc.perform(
                MockMvcRequestBuilders.post("/usuario")
                    .content(objectMapper.writeValueAsString(transferenciaRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("authorization", tokenUsuarioComum)
            )
                .andExpect(status().isOk)

            val usuarioOrigem = usuarioRepository.getReferenceById(1)
            val usuarioDestino = usuarioRepository.getReferenceById(2)

            assertEquals(
                BigDecimal(50.50).minus(transferenciaRequest.valor).setScale(2, RoundingMode.HALF_UP),
                usuarioOrigem.carteira.getSaldoAtual()
            )
            assertEquals(
                BigDecimal(2000).plus(transferenciaRequest.valor).setScale(2, RoundingMode.HALF_UP),
                usuarioDestino.carteira.getSaldoAtual()
            )

            verify(enviaEmail, times(1)).envia()
            verify(validaTransferencia, times(1)).executa()
        }

        @Test
        fun `deve retornar 401 quando o token for invalido`() {
            val transferenciaRequest = Dummy.criaTransferenciaRequest()

            mockMvc.perform(
                MockMvcRequestBuilders.post("/usuario")
                    .content(objectMapper.writeValueAsString(transferenciaRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("authorization", "token-invalido")
            )
                .andExpect(status().isUnauthorized)

            verify(enviaEmail, never()).envia()
            verify(validaTransferencia, never()).executa()
        }

        @Test
        fun `deve retornar 400 quando o usuario eh comum e nao possui saldo`() {
            `when`(validaTransferencia.executa()).thenReturn(ApiValidadoraResponse(true))
            val transferenciaRequest = Dummy.criaTransferenciaRequest(valorDaTransferencia = BigDecimal.valueOf(5000))

            mockMvc.perform(
                MockMvcRequestBuilders.post("/usuario")
                    .content(objectMapper.writeValueAsString(transferenciaRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("authorization", tokenUsuarioComum)
            )
                .andExpect(status().isBadRequest)

            verify(enviaEmail, never()).envia()
            verify(validaTransferencia, never()).executa()
        }

        @Test
        fun `deve retornar 403 quando o usuario eh comum e possui saldo e a apiValidadora retorna false`() {
            `when`(validaTransferencia.executa()).thenReturn(ApiValidadoraResponse(false))
            val transferenciaRequest = Dummy.criaTransferenciaRequest()

            mockMvc.perform(
                MockMvcRequestBuilders.post("/usuario")
                    .content(objectMapper.writeValueAsString(transferenciaRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("authorization", tokenUsuarioComum)
            )
                .andExpect(status().isForbidden)

            verify(enviaEmail, never()).envia()
            verify(validaTransferencia, times(1)).executa()
        }

        @Test
        fun `deve retornar 403 quando o usuario eh lojista`() {
            `when`(validaTransferencia.executa()).thenReturn(ApiValidadoraResponse(false))
            val transferenciaRequest = Dummy.criaTransferenciaRequest(beneficiarioId = 1)

            mockMvc.perform(
                MockMvcRequestBuilders.post("/usuario")
                    .content(objectMapper.writeValueAsString(transferenciaRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("authorization", tokenUsuarioLojista)
            )
                .andExpect(status().isForbidden)

            verify(enviaEmail, never()).envia()
            verify(validaTransferencia, never()).executa()
        }

        @Test
        fun `deve retornar 404 quando um dos usuarios nao eh localizado `() {
            `when`(validaTransferencia.executa()).thenReturn(ApiValidadoraResponse(false))
            val transferenciaRequest = Dummy.criaTransferenciaRequest(beneficiarioId = 5)

            mockMvc.perform(
                MockMvcRequestBuilders.post("/usuario")
                    .content(objectMapper.writeValueAsString(transferenciaRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("authorization", tokenUsuarioLojista)
            )
                .andExpect(status().isNotFound)

            verify(enviaEmail, never()).envia()
            verify(validaTransferencia, never()).executa()
        }

        @Test
        fun `deve retornar 503 quando a API validadora retornar algum codigo de erro`() {
            `when`(validaTransferencia.executa()).thenThrow(InternalServerError::class.java)
            val transferenciaRequest = Dummy.criaTransferenciaRequest(beneficiarioId = 5)

            mockMvc.perform(
                MockMvcRequestBuilders.post("/usuario")
                    .content(objectMapper.writeValueAsString(transferenciaRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("authorization", tokenUsuarioLojista)
            )
                .andExpect(status().isNotFound)

            verify(enviaEmail, never()).envia()
            verify(validaTransferencia, never()).executa()
        }
    }

    @Nested
    inner class TestesUnitarios {

        @Mock
        private lateinit var orquestrador: OrquestradorDeTransferencias

        @Mock
        private lateinit var preparadorDeDadosDosUsuarios: PreparadorDeDadosDosUsuarios

        @Mock
        private lateinit var enviaEmail: ApiParaEnvioDeEmail

        @InjectMocks
        private lateinit var transferenciaController: TransferenciaController

        private val valorTransferencia = BigDecimal.valueOf(200)
        private val transferenciaRequest = Dummy.criaTransferenciaRequest(valorTransferencia)
        private val authorization = "Bearer token"
        private val dadosIniciaisDaOperacao = criaDadosIniciaisDaTransacao()

        @BeforeEach
        fun setup() {
            MockitoAnnotations.openMocks(this)
            `when`(preparadorDeDadosDosUsuarios.executa(transferenciaRequest, authorization))
                .thenReturn(dadosIniciaisDaOperacao)
            `when`(orquestrador.transferirValor(dadosIniciaisDaOperacao)).then {}
            `when`(enviaEmail.envia()).then {}
        }

        @Test
        fun `deve executar transferencia com sucesso quando o metodo for chamado e houver dados validos`() {
            val response = transferenciaController.executa(transferenciaRequest, authorization)
            assertEquals("200 OK", response.statusCode.toString())

            verify(orquestrador, times(1)).transferirValor(dadosIniciaisDaOperacao)
            verify(preparadorDeDadosDosUsuarios, times(1)).executa(transferenciaRequest, authorization)
            verify(enviaEmail, times(1)).envia()
        }

        @Test
        fun `deve lancar ConstraintViolationException quando transferenciaRequest for invalido`() {
            `when`(preparadorDeDadosDosUsuarios.executa(transferenciaRequest, authorization))
                .thenThrow(ConstraintViolationException(emptySet()))

            assertThrows<ConstraintViolationException> {
                transferenciaController.executa(transferenciaRequest, authorization)
            }
        }
    }
}