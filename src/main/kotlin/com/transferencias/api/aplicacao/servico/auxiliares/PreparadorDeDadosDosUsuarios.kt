package com.transferencias.api.aplicacao.servico.auxiliares

import com.transferencias.api.aplicacao.adaptador.entrada.dto.TransferenciaRequest
import com.transferencias.api.aplicacao.adaptador.entrada.conversor.paraDestino
import com.transferencias.api.aplicacao.adaptador.entrada.conversor.paraOrigem
import com.transferencias.api.aplicacao.dominio.DadosDestino
import com.transferencias.api.aplicacao.dominio.DadosOrigem
import com.transferencias.api.aplicacao.dominio.repositorio.UsuarioRepository
import com.transferencias.api.auxiliares.jwt.JwtUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PreparadorDeDadosDosUsuarios(
    private val usuarioRepository: UsuarioRepository,
    private val jwtUtils: JwtUtils
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    fun executa(
        transferenciaRequest: TransferenciaRequest,
        authorization: String
    ): Pair<DadosOrigem, DadosDestino> {
        logger.info("[START - 02] Localizando dados dos usuarios")
        val usuarioDeOrigemId = jwtUtils.extrairUsuarioId(authorization).toLong()

        val usuarioDeOrigem = usuarioRepository
            .getReferenceById(usuarioDeOrigemId)
            .paraOrigem(transferenciaRequest.value)

        val usuarioDeDestino = usuarioRepository
            .getReferenceById(transferenciaRequest.payeeId)
            .paraDestino()

        return Pair(usuarioDeOrigem, usuarioDeDestino).also {
            logger.info("[END - 02] Usuários encontrados. Retornando dados para transferencia")
        }
    }
}