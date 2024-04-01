package com.transferencias.api.auxiliares.interceptador

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
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
import org.springframework.web.client.HttpClientErrorException.BadRequest

@RestControllerAdvice
class ValidationErrorHandler {

    @Autowired
    private lateinit var messageSource: MessageSource

    private val log: Logger = LoggerFactory.getLogger(ValidationErrorHandler::class.java)

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BadRequest::class)
    fun handleGenericException(exception: BadRequest): ValidationErrorsOutputDto {
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