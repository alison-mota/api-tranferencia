package com.transferencias.api.aplicacao.servico

import com.transferencias.api.aplicacao.dominio.DadosDestino
import com.transferencias.api.aplicacao.dominio.DadosOrigem
import com.transferencias.api.aplicacao.servico.regras.FabricaDeRegrasDeTransferencia
import com.transferencias.api.aplicacao.servico.regras.RegrasGeraisDeTransferencia
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrquestradorDeTransferencias(
    private val fabricaDeRegrasDeTransferencia: FabricaDeRegrasDeTransferencia,
    private val regrasGeraisDeTransferencia: RegrasGeraisDeTransferencia
) {
    @Transactional
    fun transferirValor(operadores: Pair<DadosOrigem, DadosDestino>) {
        regrasGeraisDeTransferencia.validaSolicitacao(operadores)

        val (usuarioDeOrigem, _) = operadores
        val (_, usuarioDeDestino) = operadores

        val operacaoRealizada = fabricaDeRegrasDeTransferencia.criaListaDeRegras
            .first { it.regraDeExecucao.name == usuarioDeOrigem.operador.name }
            .processaRegra(Pair(usuarioDeOrigem, usuarioDeDestino))

        regrasGeraisDeTransferencia.validaValorInicialEFinal(operadores, operacaoRealizada)


    }
}