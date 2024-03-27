package com.transferencias.api.aplicacao.dominio

import com.transferencias.api.aplicacao.dominio.entidade.Usuario
import java.math.BigDecimal

data class DadosOrigem(
    val usuario: Usuario,
    val valorDaTransferencia: BigDecimal
) {
    val operador: Operador = Operador.ORIGEM
    fun temSaldoSuficiente(): Boolean = valorDaTransferencia >= usuario.carteira.getSaldoAtual()
}

data class DadosDestino(
    val usuario: Usuario
) {
    val operador: Operador = Operador.DESTINO
}

enum class Operador { ORIGEM, DESTINO }