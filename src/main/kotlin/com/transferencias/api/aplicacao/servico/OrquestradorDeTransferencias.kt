package com.transferencias.api.aplicacao.servico

import com.transferencias.api.aplicacao.dominio.DadosDestino
import com.transferencias.api.aplicacao.dominio.DadosOrigem
import com.transferencias.api.aplicacao.dominio.repositorio.UsuarioRepository
import com.transferencias.api.aplicacao.servico.regras.auxiliares.FabricaDeRegrasDeTransferencia
import com.transferencias.api.aplicacao.servico.regras.gerais.ValidadoresGeraisDeTransferencia
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrquestradorDeTransferencias(
    private val fabricaDeRegrasDeTransferencia: FabricaDeRegrasDeTransferencia,
    private val validadorDeTransacao: ValidadoresGeraisDeTransferencia,
    private val usuarioRepository: UsuarioRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    @Transactional
    fun transferirValor(dadosIniciaisDaOperacao: Pair<DadosOrigem, DadosDestino>) {
        logger.info("[START - 04] Iniciando processo e validações para executar a transferencia entre contas")
        validadorDeTransacao.validaSolicitacao(dadosIniciaisDaOperacao)

        val (usuarioDeOrigem, _) = dadosIniciaisDaOperacao
        val operacaoRealizada = fabricaDeRegrasDeTransferencia.criaListaDeRegras
            .first { it.regraDeExecucao.name == usuarioDeOrigem.usuario.perfil.name }
            .processaRegra(dadosIniciaisDaOperacao)

        validadorDeTransacao.validacoesFinais(dadosIniciaisDaOperacao, operacaoRealizada)

        usuarioRepository.saveAll(listOf(operacaoRealizada.first.usuario, operacaoRealizada.second.usuario))
        logger.info("[END - 04] Transferencia entre contas realizada")
    }
}