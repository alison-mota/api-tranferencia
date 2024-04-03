package com.transferencias.api.helpers

import com.transferencias.api.aplicacao.adaptador.entrada.dto.TransferenciaRequest
import com.transferencias.api.aplicacao.dominio.DadosDestino
import com.transferencias.api.aplicacao.dominio.DadosOrigem
import com.transferencias.api.aplicacao.dominio.entidade.Usuario
import java.math.BigDecimal

fun generateToken(usuarioId: Long): String = when (usuarioId) {
    1L -> "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcGktdHJhbnNmZXJlbmNpYSIsInVzdWFyaW9faWQiOjF9.6olKn5uNY5kR0tT41c76erh2h_pKZjIikBYZN0Bpx1Y"
    2L -> "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcGktdHJhbnNmZXJlbmNpYSIsInVzdWFyaW9faWQiOjJ9.6G7LT-IFwDP6dHylCLnbqpFJ7PZLtT8l6zFwW2hAGGk"
    3L -> "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcGktdHJhbnNmZXJlbmNpYSIsInVzdWFyaW9faWQiOjN9.Ry0VJH52c6s-g3V8fBvXmj5ynIxSr_ENO4zC_GnDDd8"
    4L -> "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcGktdHJhbnNmZXJlbmNpYSIsInVzdWFyaW9faWQiOjR9.klOMX-EkGgClUW2zLfV9ZKO3TceBfGS7gRVZtRPjIHw"
    else -> ""
}

object Dummy {
    fun criaTransferenciaRequest(
        valorDaTransferencia: BigDecimal = BigDecimal.TEN,
        beneficiarioId: Long = 2
    ) = TransferenciaRequest(valorDaTransferencia, beneficiarioId)

    fun criaDadosIniciaisDaTransacao() = Pair(
        DadosOrigem(
            usuario = criaUsuarioComum(),
            valorDaTransferencia = BigDecimal.valueOf(1000)
        ),
        DadosDestino(
            usuario = criaUsuarioLojista()
        )
    )

    fun criaUsuarioComum(
        nome: String = "Joao",
        cpfOuCnpj: String = "13721313020",
        email: String = "email@email.com",
        senha: String = "senha",
        perfil: Usuario.Perfil = Usuario.Perfil.COMUM,
        carteira: Usuario.Carteira = Usuario.Carteira().adicionaSaldo(BigDecimal.valueOf(1000.0))
    ) = Usuario(
        nome = nome,
        cpfOuCnpj = cpfOuCnpj,
        email = email,
        senha = senha,
        perfil = perfil,
        carteira = carteira
    )

    fun criaUsuarioLojista() = criaUsuarioComum(perfil = Usuario.Perfil.LOJISTA)
}