package com.transferencias.api.auxiliares.interceptador

class ValidationErrorsOutputDto {
    private val globalErrorMessages: MutableList<String?> = ArrayList()
    private val fieldErrors: MutableList<FieldErrorOutputDto> = ArrayList()

    fun addError(message: String?) {
        globalErrorMessages.add(message)
    }

    fun addFieldError(field: String, message: String) {
        val fieldError = FieldErrorOutputDto(field, message)
        fieldErrors.add(fieldError)
    }

    fun getGlobalErrorMessages(): List<String?> {
        return globalErrorMessages
    }

    fun getErrors(): List<FieldErrorOutputDto> {
        return fieldErrors
    }

    fun getNumberOfErrors(): Int {
        return globalErrorMessages.size + fieldErrors.size
    }
}
