package com.transferencias.api.auxiliares.interceptador

import com.transferencias.api.auxiliares.excecoes.*
import feign.FeignException.BadRequest
import feign.FeignException.InternalServerError
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ValidationErrorHandler {

    @Autowired
    private lateinit var messageSource: MessageSource

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
        val globalErrors = listOf(ObjectError("", exception.message))
        val fieldErrors = emptyList<FieldError>()

        return buildValidationErrors(globalErrors, fieldErrors)
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(JpaObjectRetrievalFailureException::class)
    fun handleValidationError(exception: JpaObjectRetrievalFailureException): ValidationErrorsOutputDto {
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

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenInvalidoException::class)
    fun handleGenericException(exception: TokenInvalidoException): ValidationErrorsOutputDto {
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequest::class)
    fun handleGenericException(exception: BadRequest): ValidationErrorsOutputDto {
        val validationErrors = ValidationErrorsOutputDto()
        validationErrors.addError("Houve um problema ao tentar validar a transferência")

        return validationErrors
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException::class)
    fun handleGenericException(exception: MissingRequestHeaderException): ValidationErrorsOutputDto {
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

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(InternalServerError::class)
    fun handleGenericException(exception: InternalServerError): ValidationErrorsOutputDto {
        val validationErrors = ValidationErrorsOutputDto()
        validationErrors.addError("O serviço está temporariamente indisponível, tente novamente mais tarde")

        return validationErrors
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UsuarioNaoAutorizadoException::class)
    fun handleGenericException(exception: UsuarioNaoAutorizadoException): ValidationErrorsOutputDto {
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