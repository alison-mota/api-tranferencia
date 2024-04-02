package com.transferencias.api.aplicacao.adaptador.saida.apivalidadora.api

import feign.Retryer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(ApiValidadoraProperties::class)
class ApiValidadoraConfig {

    @Autowired
    lateinit var apiValidadoraProperties: ApiValidadoraProperties

    @Bean
    fun retryer(): Retryer {
        return Retryer.Default(
            apiValidadoraProperties.period,
            apiValidadoraProperties.timeout,
            apiValidadoraProperties.retryCount
        )
    }
}


@ConfigurationProperties(prefix = "feign.api-validadora.client")
data class ApiValidadoraProperties(
    var period: Long,
    var timeout: Long,
    var retryCount: Int
)