package com.transferencias.api.aplicacao.servico.regras

import com.transferencias.api.aplicacao.dominio.DadosDestino
import com.transferencias.api.aplicacao.dominio.DadosOrigem
import com.transferencias.api.aplicacao.servico.auxiliares.ExecutorDeTransferencia
import com.transferencias.api.aplicacao.servico.contratos.RegraDeTransferencia
import com.transferencias.api.aplicacao.servico.contratos.TipoDeRegra
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TransferenciaDeUsuarioComum(
    override val regraDeExecucao: TipoDeRegra = TipoDeRegra.COMUM
) : RegraDeTransferencia {

    @Autowired
    lateinit var executorDeTransferencia: ExecutorDeTransferencia
    override fun processaRegra(
        operadores: Pair<DadosOrigem, DadosDestino>
    ): Pair<DadosOrigem, DadosDestino> = executorDeTransferencia.executa(operadores)
}