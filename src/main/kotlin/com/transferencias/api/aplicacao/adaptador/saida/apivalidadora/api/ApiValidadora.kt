package com.transferencias.api.aplicacao.adaptador.saida.apivalidadora.api

import com.transferencias.api.aplicacao.adaptador.saida.apivalidadora.dto.ApiValidadoraResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(
    name = "ApiValidadora",
    url = "\${api.validadora}",
    configuration = [ApiValidadoraConfig::class]
)
interface ApiValidadora {
    @GetMapping("/api")
    fun executa(): ApiValidadoraResponse
}