package com.transferencias.api.aplicacao.adaptador.controller

import com.transferencias.api.aplicacao.adaptador.dto.TransferenciaRequest
import com.transferencias.api.aplicacao.dominio.DadosDestino
import com.transferencias.api.aplicacao.dominio.DadosOrigem
import com.transferencias.api.aplicacao.dominio.entidade.Usuario
import com.transferencias.api.aplicacao.dominio.repositorio.UsuarioRepository
import com.transferencias.api.aplicacao.servico.OrquestradorDeTransferencias
import com.transferencias.api.auxiliares.jwt.JwtUtils
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/usuario")
class TransferenciaController(
    private val orquestradorDeTransferencias: OrquestradorDeTransferencias,
    private val usuarioRepository: UsuarioRepository,
    private val jwtUtils: JwtUtils
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping
    fun executa(
        @Valid @RequestBody transferenciaRequest: TransferenciaRequest,
        @RequestHeader authorization: String
    ): ResponseEntity<Any> {
        logger.info("[START - 01] Iniciando transferencia entre contas")

        val usuarioDeOrigemId = jwtUtils.extrairUsuarioId(authorization)

        val usuarioDeOrigem = usuarioRepository
            .getReferenceById(usuarioDeOrigemId.toLong())
            .paraOrigem(transferenciaRequest.valor)

        val usuarioDeDestino = usuarioRepository
            .getReferenceById(transferenciaRequest.beneficiario)
            .paraDestino()

        val pair: Pair<DadosOrigem, DadosDestino> = Pair(usuarioDeOrigem, usuarioDeDestino)

        orquestradorDeTransferencias.transferirValor(pair)

        return ResponseEntity.ok()
            .build<Any?>()
            .also {
                logger.info("[END - 01] Transferencia entre contas finalizado")
            }
    }
}

private fun Usuario.paraDestino() = DadosDestino(usuario = this)

private fun Usuario.paraOrigem(valorDaTransferencia: BigDecimal): DadosOrigem = DadosOrigem(
    usuario = this,
    valorDaTransferencia = valorDaTransferencia
)
