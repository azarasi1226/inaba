package jp.inaba.catalog.service.presentation.common

import jp.inaba.common.domain.shared.ValueObjectException
import jp.inaba.common.presentation.shared.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CommonAdvice {
    @ExceptionHandler(ValueObjectException::class)
    fun handle(ex: ValueObjectException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(ex.errorMessage, ex.errorCode),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(Exception::class)
    fun handle(ex: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(ex.message),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}