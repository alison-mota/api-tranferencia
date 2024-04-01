package com.transferencias.api.aplicacao.servico.regras

import com.transferencias.api.aplicacao.dominio.DadosDestino
import com.transferencias.api.aplicacao.dominio.DadosOrigem
import com.transferencias.api.aplicacao.servico.contratos.RegraDeTransferencia
import com.transferencias.api.aplicacao.servico.contratos.TipoDeRegra
import com.transferencias.api.auxiliares.excecoes.UsuarioNaoPermitidoException
import org.springframework.stereotype.Component

@Component
class TransferenciaDeLojista(
    override val regraDeExecucao: TipoDeRegra = TipoDeRegra.LOJISTA
) : RegraDeTransferencia {
    override fun processaRegra(operadores: Pair<DadosOrigem, DadosDestino>): Pair<DadosOrigem, DadosDestino> {
        /*
          Usuário do tipo LOJISTA não está autorizado a realizar transferências.
         */
        throw UsuarioNaoPermitidoException()
    }
}