package com.transferencias.api.aplicacao.servico

import com.transferencias.api.aplicacao.dominio.DadosDestino
import com.transferencias.api.aplicacao.dominio.DadosOrigem
import com.transferencias.api.aplicacao.dominio.repositorio.UsuarioRepository
import com.transferencias.api.aplicacao.servico.regras.FabricaDeRegrasDeTransferencia
import com.transferencias.api.aplicacao.servico.regras.ValidadoresGeraisDeTransferencia
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrquestradorDeTransferencias(
    private val fabricaDeRegrasDeTransferencia: FabricaDeRegrasDeTransferencia,
    private val validadoresGeraisDeTransferencia: ValidadoresGeraisDeTransferencia,
    private val usuarioRepository: UsuarioRepository
) {
    @Transactional
    fun transferirValor(operadores: Pair<DadosOrigem, DadosDestino>) {
        validadoresGeraisDeTransferencia.validaSolicitacao(operadores)

        val (usuarioDeOrigem, _) = operadores
        val (_, usuarioDeDestino) = operadores

        val operacaoRealizada = fabricaDeRegrasDeTransferencia.criaListaDeRegras
            .first { it.regraDeExecucao.name == usuarioDeOrigem.usuario.perfil.name }
            .processaRegra(Pair(usuarioDeOrigem, usuarioDeDestino))

        validadoresGeraisDeTransferencia.validaValorInicialEFinal(operadores, operacaoRealizada)

        usuarioRepository.saveAll(listOf(operacaoRealizada.first.usuario, operacaoRealizada.second.usuario))
    }
}