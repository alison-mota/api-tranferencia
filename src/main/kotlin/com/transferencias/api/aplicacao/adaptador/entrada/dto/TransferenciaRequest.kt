package com.transferencias.api.aplicacao.adaptador.entrada.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class TransferenciaRequest(
    @NotNull
    @Positive
    val value: BigDecimal,
    @NotNull
    @JsonProperty("payee_id")
    val payeeId: Long
)