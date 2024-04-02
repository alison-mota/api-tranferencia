package com.transferencias.api.aplicacao.adaptador.entrada.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.transferencias.api.aplicacao.adaptador.saida.apivalidadora.api.ApiValidadora
import com.transferencias.api.aplicacao.adaptador.saida.apivalidadora.dto.ApiValidadoraResponse
import com.transferencias.api.aplicacao.adaptador.saida.email.api.EnvioDeEmail
import com.transferencias.api.helpers.Dummy
import com.transferencias.api.helpers.generateToken
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
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
import java.math.BigDecimal

class TransferenciaControllerTest(
) {
    @Nested
    @ExtendWith(SpringExtension::class)
    @SpringBootTest
    @AutoConfigureMockMvc
    @ActiveProfiles("test")
    inner class TestesIntegrados {

        @Autowired
        private lateinit var mockMvc: MockMvc

        @Autowired
        private lateinit var objectMapper: ObjectMapper

        @MockBean
        private lateinit var enviaEmail: EnvioDeEmail

        @MockBean
        private lateinit var validaTransferencia: ApiValidadora

        @BeforeEach
        fun setup() {
        }

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
                .andExpect(status().isForbidden)
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
        }
    }

    @Nested
    inner class TestesUnitarios {}
}