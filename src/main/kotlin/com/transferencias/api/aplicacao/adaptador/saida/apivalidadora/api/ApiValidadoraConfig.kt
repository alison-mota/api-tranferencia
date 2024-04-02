package com.transferencias.api.aplicacao.adaptador.saida.apivalidadora.api

import feign.Retryer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.context.annotation.Bean

const val PREFIX_CONFIG = "feign.api-validadora.client"
class ApiValidadoraConfig {
    @Bean
    fun retryer(apiValidadoraProperties: ApiValidadoraProperties): Retryer =
        apiValidadoraProperties.run{
            Retryer.Default(period, timeout, retryCount)
        }
}

data class ApiValidadoraProperties(
    @Value("\${$PREFIX_CONFIG.period}") val period: Long,
    @Value("\${$PREFIX_CONFIG.timeout}")val timeout: Long,
    @Value("\${$PREFIX_CONFIG.retry-count}")val retryCount: Int
)