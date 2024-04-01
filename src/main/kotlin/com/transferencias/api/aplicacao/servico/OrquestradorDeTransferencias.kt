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
    private val validadoresGeraisDeTransferencia: ValidadoresGeraisDeTransferencia,
    private val usuarioRepository: UsuarioRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    @Transactional
    fun transferirValor(operadores: Pair<DadosOrigem, DadosDestino>) {
        logger.info("[START - 04] Iniciando processo e validações para executar a transferencia entre contas")
        validadoresGeraisDeTransferencia.validaSolicitacao(operadores)

        val (usuarioDeOrigem, _) = operadores
        val operacaoRealizada = fabricaDeRegrasDeTransferencia.criaListaDeRegras
            .first { it.regraDeExecucao.name == usuarioDeOrigem.usuario.perfil.name }
            .processaRegra(operadores)

        validadoresGeraisDeTransferencia.validaValorInicialEFinal(operadores, operacaoRealizada)
        usuarioRepository.saveAll(listOf(operacaoRealizada.first.usuario, operacaoRealizada.second.usuario))
        logger.info("[END - 04] Transferencia entre contas realizada")
    }
}