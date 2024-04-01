package com.transferencias.api.aplicacao.adaptador

import com.transferencias.api.aplicacao.dominio.DadosDestino
import com.transferencias.api.aplicacao.dominio.DadosOrigem
import com.transferencias.api.aplicacao.dominio.entidade.Usuario
import java.math.BigDecimal

fun Usuario.paraDestino() = DadosDestino(usuario = this)

fun Usuario.paraOrigem(valorDaTransferencia: BigDecimal): DadosOrigem = DadosOrigem(
    usuario = this,
    valorDaTransferencia = valorDaTransferencia
)