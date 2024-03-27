package com.transferencias.api.aplicacao.adaptador.controller

import com.transferencias.api.aplicacao.adaptador.dto.TransferenciaRequest
import com.transferencias.api.aplicacao.dominio.repositorio.UsuarioRepository
import jakarta.validation.Valid
import org.apache.tomcat.util.http.parser.Authorization
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/usuario")
class TransferenciaController (
    private val usuarioRepository: UsuarioRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    @PostMapping
    fun executa(
        @Valid @RequestBody transferenciaRequest: TransferenciaRequest,
        @RequestHeader authorization: Authorization
    ) {}
}