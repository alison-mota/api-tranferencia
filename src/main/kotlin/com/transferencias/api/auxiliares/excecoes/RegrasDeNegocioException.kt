package com.transferencias.api.auxiliares.excecoes

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class SaldoInsuficienteException(
    mensagem: String = "Saldo insuficiente"
) : RuntimeException(mensagem)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class UsuarioNaoPermitidoException(
    mensagem: String = "Esse perfil de usuário não pode realizar transferências"
) : RuntimeException(mensagem)