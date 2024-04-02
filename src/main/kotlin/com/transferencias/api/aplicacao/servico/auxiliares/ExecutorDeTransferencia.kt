package com.transferencias.api.aplicacao.servico.auxiliares

import com.transferencias.api.aplicacao.dominio.DadosDestino
import com.transferencias.api.aplicacao.dominio.DadosOrigem
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ExecutorDeTransferencia {
    private val logger = LoggerFactory.getLogger(this::class.java)
    fun executa(
        operadores: Pair<DadosOrigem, DadosDestino>
    ): Pair<DadosOrigem, DadosDestino> {

        logger.info(
            "[START - 05] Executando a transferencia de R$${operadores.first.valorDaTransferencia}. " +
                    "Usuario de origem: ${operadores.first.usuario.nome}. " +
                    "Usuario de destino: ${operadores.second.usuario.nome}"
        )

        val (usuarioDeOrigem, _) = operadores
        val (_, usuarioDeDestino) = operadores

        val valorTransferencia = usuarioDeOrigem.valorDaTransferencia
        usuarioDeOrigem.usuario.carteira = usuarioDeOrigem.usuario.carteira.subtraiSaldo(valorTransferencia)
        usuarioDeDestino.usuario.carteira = usuarioDeDestino.usuario.carteira.adicionaSaldo(valorTransferencia)

        return Pair(
            usuarioDeOrigem,
            usuarioDeDestino
        ).also {
            logger.info("[END - 05] Transferencia de R$${it.first.valorDaTransferencia} computada. Aguardando finalização")
        }
    }
}