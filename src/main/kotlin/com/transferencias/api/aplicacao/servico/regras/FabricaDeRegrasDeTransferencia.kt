package com.transferencias.api.aplicacao.servico.regras

import com.transferencias.api.aplicacao.servico.contratos.RegraDeTransferencia
import org.springframework.stereotype.Component

@Component
class FabricaDeRegrasDeTransferencia(regras: List<RegraDeTransferencia>) {
    val criaListaDeRegras: List<RegraDeTransferencia> = regras
        .sortedBy { it.regraDeExecucao.ordinal }
}