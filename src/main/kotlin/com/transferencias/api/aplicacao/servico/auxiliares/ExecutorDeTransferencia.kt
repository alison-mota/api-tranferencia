package com.transferencias.api.aplicacao.servico.auxiliares

import com.transferencias.api.aplicacao.dominio.DadosDestino
import com.transferencias.api.aplicacao.dominio.DadosOrigem
import org.springframework.stereotype.Service

@Service
class ExecutorDeTransferencia {
    fun executa(
        operadores: Pair<DadosOrigem, DadosDestino>
    ): Pair<DadosOrigem, DadosDestino> {

        val (usuarioDeOrigem, _) = operadores
        val (_, usuarioDeDestino) = operadores

        val valorTransferencia = usuarioDeOrigem.valorDaTransferencia
        usuarioDeOrigem.usuario.carteira = usuarioDeOrigem.usuario.carteira.subtraiSaldo(valorTransferencia)
        usuarioDeDestino.usuario.carteira = usuarioDeDestino.usuario.carteira.adicionaSaldo(valorTransferencia)

        return Pair(
            usuarioDeOrigem,
            usuarioDeDestino
        )
    }
}