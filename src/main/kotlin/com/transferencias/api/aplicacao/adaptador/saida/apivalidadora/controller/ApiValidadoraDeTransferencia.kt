package com.transferencias.api.aplicacao.adaptador.saida.apivalidadora.controller

import com.transferencias.api.aplicacao.adaptador.saida.apivalidadora.api.ApiValidadora
import com.transferencias.api.aplicacao.adaptador.saida.apivalidadora.dto.ApiValidadoraResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ApiValidadoraDeTransferencia(
    private val apiValidadora: ApiValidadora
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun valida(): ApiValidadoraResponse {
        return try {
            apiValidadora.executa()
        } catch (ex: Exception) {
            logger.error("Houve um problema ao tentar validar a transferência. {}", ex.message)
            throw ex
        }
    }
}