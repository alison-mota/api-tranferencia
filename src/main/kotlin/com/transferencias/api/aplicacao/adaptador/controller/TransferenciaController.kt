package com.transferencias.api.aplicacao.adaptador.controller

import com.transferencias.api.aplicacao.adaptador.dto.TransferenciaRequest
import com.transferencias.api.aplicacao.dominio.repositorio.UsuarioRepository
import com.transferencias.api.aplicacao.servico.OrquestradorDeTransferencias
import com.transferencias.api.auxiliares.jwt.JwtUtils
import jakarta.validation.Valid
import org.apache.tomcat.util.http.parser.Authorization
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/usuario")
class TransferenciaController (
    private val orquestradorDeTransferencias: OrquestradorDeTransferencias,
    private val jwtUtils: JwtUtils
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    @PostMapping
    fun executa(
        @Valid @RequestBody transferenciaRequest: TransferenciaRequest,
        @RequestHeader authorization: String
    ): ResponseEntity<Any> {
        logger.info("[START - 01] Iniciando transferencia entre contas")

        logger.info(">>>>>>>>>>>>>>>>>    ${jwtUtils.extrairUsuarioId(authorization)}")

        return ResponseEntity.ok()
            .build<Any?>()
            .also {
                logger.info("[END - 01] Transferencia entre contas finalizado")
            }
    }
}