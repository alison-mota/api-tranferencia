package com.transferencias.api.aplicacao.servico.regras

import com.transferencias.api.aplicacao.dominio.DadosDestino
import com.transferencias.api.aplicacao.dominio.DadosOrigem
import com.transferencias.api.aplicacao.servico.contratos.RegraDeTransferencia
import com.transferencias.api.aplicacao.servico.contratos.TipoDeRegra
import org.springframework.stereotype.Component

@Component
class TransferenciaDeUsuarioComum(
    override val regraDeExecucao: TipoDeRegra = TipoDeRegra.COMUM
) : RegraDeTransferencia {
    override fun processaRegra(operadores: Pair<DadosOrigem, DadosDestino>): Pair<DadosOrigem, DadosDestino> {
        TODO("Not yet implemented")
    }
}