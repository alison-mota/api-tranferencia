package com.transferencias.api.aplicacao.adaptador.saida.apivalidadora.api

import com.transferencias.api.aplicacao.adaptador.saida.apivalidadora.dto.ApiValidadoraResponse
import com.transferencias.api.aplicacao.adaptador.saida.email.api.EnvioDeEmailConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(
    name = "ApiValidadora",
    url = "\${api.externa.validadora}",
    configuration = [EnvioDeEmailConfig::class]
)
interface ApiValidadora {
    @GetMapping("/api")
    fun executa(): ApiValidadoraResponse
}