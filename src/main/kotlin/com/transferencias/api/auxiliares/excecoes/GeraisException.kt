package com.transferencias.api.auxiliares.excecoes

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class TokenInvalidoException(
    message: String = "Token inválido"
) : RuntimeException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BusinessException(
    mensagem: String = "Houve um problema com a sua requisição"
) : RuntimeException(mensagem)

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
class ServicoIndisponivelException(
    mensagem: String = "Serviço indisponível no momento. Tente novamente mais tarde"
) : RuntimeException(mensagem)