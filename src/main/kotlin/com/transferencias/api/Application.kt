package com.transferencias.api

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
@OpenAPIDefinition
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}