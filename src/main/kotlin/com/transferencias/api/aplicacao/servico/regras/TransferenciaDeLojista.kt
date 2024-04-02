package com.transferencias.api.aplicacao.servico.regras

import com.transferencias.api.aplicacao.dominio.DadosDestino
import com.transferencias.api.aplicacao.dominio.DadosOrigem
import com.transferencias.api.aplicacao.servico.contratos.RegraDeTransferencia
import com.transferencias.api.aplicacao.servico.contratos.TipoDeRegra
import com.transferencias.api.auxiliares.excecoes.UsuarioNaoPermitidoException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class TransferenciaDeLojista(
    override val regraDeExecucao: TipoDeRegra = TipoDeRegra.LOJISTA
) : RegraDeTransferencia {
    private val logger = LoggerFactory.getLogger(this::class.java)
    override fun processaRegra(operadores: Pair<DadosOrigem, DadosDestino>): Pair<DadosOrigem, DadosDestino> {
        logger.error("Usuário do tipo LOJISTA não está autorizado para realizar transferências")
        throw UsuarioNaoPermitidoException()
    }
}