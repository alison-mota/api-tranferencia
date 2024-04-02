package com.transferencias.api.auxiliares.interceptador

import com.transferencias.api.auxiliares.excecoes.BusinessException
import com.transferencias.api.auxiliares.excecoes.SaldoInsuficienteException
import com.transferencias.api.auxiliares.excecoes.ServicoIndisponivelException
import com.transferencias.api.auxiliares.excecoes.UsuarioNaoPermitidoException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ValidationErrorHandler {

    @Autowired
    private lateinit var messageSource: MessageSource

    private val log: Logger = LoggerFactory.getLogger(ValidationErrorHandler::class.java)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SaldoInsuficienteException::class)
    fun handleGenericException(exception: SaldoInsuficienteException): ValidationErrorsOutputDto {
        val validationErrors = ValidationErrorsOutputDto()
        validationErrors.addError(exception.message)

        return validationErrors
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleValidationError(exception: HttpMessageNotReadableException): ValidationErrorsOutputDto {
        log.error("Problema para desserializar o objeto", exception)

        val globalErrors = listOf(ObjectError("", exception.message))
        val fieldErrors = emptyList<FieldError>()

        return buildValidationErrors(globalErrors, fieldErrors)
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UsuarioNaoPermitidoException::class)
    fun handleGenericException(exception: UsuarioNaoPermitidoException): ValidationErrorsOutputDto {
        val validationErrors = ValidationErrorsOutputDto()
        validationErrors.addError(exception.message)

        return validationErrors
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException::class)
    fun handleGenericException(exception: BusinessException): ValidationErrorsOutputDto {
        val validationErrors = ValidationErrorsOutputDto()
        validationErrors.addError(exception.message)

        return validationErrors
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ServicoIndisponivelException::class)
    fun handleGenericException(exception: ServicoIndisponivelException): ValidationErrorsOutputDto {
        val validationErrors = ValidationErrorsOutputDto()
        validationErrors.addError(exception.message)

        return validationErrors
    }

    private fun buildValidationErrors(
        globalErrors: List<ObjectError>,
        fieldErrors: List<FieldError>
    ): ValidationErrorsOutputDto {
        val validationErrors = ValidationErrorsOutputDto()

        globalErrors.forEach { error -> validationErrors.addError(getErrorMessage(error)) }

        fieldErrors.forEach { error ->
            val errorMessage = getErrorMessage(error)
            validationErrors.addFieldError(error.field, errorMessage)
        }

        return validationErrors
    }

    private fun getErrorMessage(error: ObjectError): String {
        return messageSource.getMessage(error, LocaleContextHolder.getLocale())
    }
}