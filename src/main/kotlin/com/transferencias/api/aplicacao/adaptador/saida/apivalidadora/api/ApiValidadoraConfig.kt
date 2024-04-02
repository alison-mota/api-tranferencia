package com.transferencias.api.aplicacao.adaptador.saida.apivalidadora.api

import com.transferencias.api.aplicacao.adaptador.saida.email.api.EnvioDeEmailProperties
import feign.Logger
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
    lateinit var apiValidadoraProperties: EnvioDeEmailProperties

    @Bean
    fun retryer(): Retryer {
        return Retryer.Default(
            apiValidadoraProperties.period,
            apiValidadoraProperties.timeout,
            apiValidadoraProperties.retryCount
        )
    }

    @Bean
    fun feignLoggerLevel(): Logger.Level? {
        return Logger.Level.NONE
    }
}


@ConfigurationProperties(prefix = "feign.api-validadora.client")
data class ApiValidadoraProperties(
    var period: Long,
    var timeout: Long,
    var retryCount: Int
)