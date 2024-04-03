package com.transferencias.api.aplicacao.adaptador.entrada.conversor

import com.transferencias.api.helpers.Dummy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class ConversoresTest {

    private val usuario = Dummy.criaUsuarioComum()

    @Test
    fun `paraDestino() deve retornar DadosDestino com o usuario correto`() {
        val dadosDestino = usuario.paraDestino()
        assertEquals(usuario, dadosDestino.usuario)
    }

    @Test
    fun `paraOrigem() deve retornar DadosOrigem com o usuario e valor de transferencia corretos`() {
        val valorDaTransferencia = BigDecimal("100.00")
        val dadosOrigem = usuario.paraOrigem(valorDaTransferencia)
        assertEquals(usuario, dadosOrigem.usuario)
        assertEquals(valorDaTransferencia, dadosOrigem.valorDaTransferencia)
    }
}