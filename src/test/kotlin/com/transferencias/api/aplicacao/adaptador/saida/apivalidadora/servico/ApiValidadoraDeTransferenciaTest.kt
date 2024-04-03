package com.transferencias.api.aplicacao.adaptador.saida.apivalidadora.servico

import com.transferencias.api.aplicacao.adaptador.saida.apivalidadora.api.ApiValidadora
import com.transferencias.api.aplicacao.adaptador.saida.apivalidadora.dto.ApiValidadoraResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class ApiValidadoraDeTransferenciaTest {

    @Mock
    private lateinit var apiValidadora: ApiValidadora

    @InjectMocks
    private lateinit var apiValidadoraDeTransferencia: ApiValidadoraDeTransferencia

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `deve retornar ApiValidadoraResponse quando a validacao for bem sucedida`() {
        val respostaEsperada = ApiValidadoraResponse(true)
        `when`(apiValidadora.executa()).thenReturn(respostaEsperada)
        val resposta = apiValidadoraDeTransferencia.valida()

        assertEquals(respostaEsperada, resposta)
    }

    @Test
    fun `deve lancar excecao quando a validacao falhar`() {
        `when`(apiValidadora.executa()).thenThrow(RuntimeException())

        assertThrows<RuntimeException> {
            apiValidadoraDeTransferencia.valida()
        }
    }
}
