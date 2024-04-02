package com.transferencias.api.aplicacao.adaptador.entrada.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class TransferenciaRequest(
    @NotNull
    @Positive
    val valor: BigDecimal,
    @NotNull
    val beneficiario: Long
)