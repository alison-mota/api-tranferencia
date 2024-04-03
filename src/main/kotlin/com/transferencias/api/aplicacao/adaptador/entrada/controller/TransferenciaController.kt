package com.transferencias.api.aplicacao.adaptador.entrada.controller

import com.transferencias.api.aplicacao.adaptador.entrada.dto.TransferenciaRequest
import com.transferencias.api.aplicacao.adaptador.saida.email.servico.ApiParaEnvioDeEmail
import com.transferencias.api.aplicacao.servico.OrquestradorDeTransferencias
import com.transferencias.api.aplicacao.servico.auxiliares.PreparadorDeDadosDosUsuarios
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuario")
@Tag(name = "api-de-transferencia")
class TransferenciaController(
    private val orquestrador: OrquestradorDeTransferencias,
    private val preparadorDeDadosDosUsuarios: PreparadorDeDadosDosUsuarios,
    private val enviaEmail: ApiParaEnvioDeEmail
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Operation(summary = "Responsável por realizar transferências de valores entre usuários", method = "POST")
    @ApiResponse(responseCode = "200", description = "Ok - Transferência realizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Bad Request - Erro na validação dos dados da transferência")
    @ApiResponse(responseCode = "401", description = "Unauthorized - Token de autenticação inválido")
    @ApiResponse(
        responseCode = "403",
        description = "Forbidden - Serviço não autorizado ou perfil de usuário não pode realizar transferências"
    )
    @ApiResponse(
        responseCode = "404",
        description = "Not Found - Os usuários da requisição não foram encontrados no banco de dados"
    )
    @ApiResponse(responseCode = "503", description = "Service Unavaiable - Erro interno no servidor")
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