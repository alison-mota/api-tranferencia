package com.transferencias.api.aplicacao.adaptador.saida.email.api

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(
    name = "EnvioDeEmail",
    url = "\${api.externa.email}",
    configuration = [EnvioDeEmailConfig::class]
)
interface EnvioDeEmail {
    @PostMapping("/email")
    fun envia()
}