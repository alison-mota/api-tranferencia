package com.transferencias.api.aplicacao.adaptador.entrada.controller

import com.transferencias.api.aplicacao.adaptador.entrada.dto.TransferenciaRequest
import com.transferencias.api.aplicacao.adaptador.saida.email.servico.ApiParaEnvioDeEmail
import com.transferencias.api.aplicacao.servico.OrquestradorDeTransferencias
import com.transferencias.api.aplicacao.servico.auxiliares.PreparadorDeDadosDosUsuarios
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuario")
class TransferenciaController(
    private val orquestrador: OrquestradorDeTransferencias,
    private val preparadorDeDadosDosUsuarios: PreparadorDeDadosDosUsuarios,
    private val enviaEmail: ApiParaEnvioDeEmail
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping
    fun executa(
        @Valid @RequestBody transferenciaRequest: TransferenciaRequest,
        @RequestHeader(required = true) authorization: String
    ): ResponseEntity<Any> {
        logger.info("[START - 01] Iniciando transferencia entre contas")

        val dadosIniciaisDaOperacao = preparadorDeDadosDosUsuarios.executa(transferenciaRequest, authorization)
        orquestrador.transferirValor(dadosIniciaisDaOperacao)
        enviaEmail.envia()

        return ResponseEntity.ok()
            .build<Any?>()
            .also {
                logger.info("[END - 01] Transferencia entre contas finalizada")
            }
    }
}