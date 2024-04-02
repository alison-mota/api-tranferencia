package com.transferencias.api.aplicacao.adaptador.saida.email.api

import feign.Logger
import feign.Retryer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(EnvioDeEmailProperties::class)
@EnableFeignClients(basePackages = ["com.transferencias.api.aplicacao.adaptador.saida.email.api"])
class EnvioDeEmailConfig {

    @Autowired
    lateinit var envioDeEmailProperties: EnvioDeEmailProperties

    @Bean
    fun retryer(): Retryer {
        return Retryer.Default(
            envioDeEmailProperties.period,
            envioDeEmailProperties.timeout,
            envioDeEmailProperties.retryCount
        )
    }

    @Bean
    fun feignLoggerLevel(): Logger.Level? {
        return Logger.Level.NONE
    }
}


@ConfigurationProperties(prefix = "feign.api-email.client")
data class EnvioDeEmailProperties(
    var period: Long,
    var timeout: Long,
    var retryCount: Int
)