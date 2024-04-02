package com.transferencias.api.aplicacao.adaptador.entrada.controller

import com.transferencias.api.aplicacao.adaptador.entrada.dto.TransferenciaRequest
import com.transferencias.api.aplicacao.servico.OrquestradorDeTransferencias
import com.transferencias.api.aplicacao.servico.auxiliares.LocalizadorDeUsuarios
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuario")
class TransferenciaController(
    private val orquestradorDeTransferencias: OrquestradorDeTransferencias,
    private val localizadorDeUsuarios: LocalizadorDeUsuarios
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping
    fun executa(
        @Valid @RequestBody transferenciaRequest: TransferenciaRequest,
        @RequestHeader authorization: String
    ): ResponseEntity<Any> {
        logger.info("[START - 01] Iniciando transferencia entre contas")

        orquestradorDeTransferencias.transferirValor(localizadorDeUsuarios.executa(transferenciaRequest, authorization))

        return ResponseEntity.ok()
            .build<Any?>()
            .also {
                logger.info("[END - 01] Transferencia entre contas finalizada")
            }
    }
}