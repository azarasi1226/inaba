package jp.inaba.catalog.service.presentation.common

import jp.inaba.common.domain.shared.DomainException
import jp.inaba.common.presentation.shared.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CommonAdvice {
    @ExceptionHandler(DomainException::class)
    fun handle(ex: DomainException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(ex.errorMessage, ex.errorCode),
            HttpStatus.BAD_REQUEST,
        )
    }
}
