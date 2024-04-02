package com.transferencias.api.aplicacao.servico.contratos

import com.transferencias.api.aplicacao.dominio.DadosDestino
import com.transferencias.api.aplicacao.dominio.DadosOrigem

interface RegraDeTransferencia {
    val regraDeExecucao: TipoDeRegra
    fun processaRegra(operadores: Pair<DadosOrigem, DadosDestino>): Pair<DadosOrigem, DadosDestino>
}

enum class TipoDeRegra {
    LOJISTA,
    COMUM
}