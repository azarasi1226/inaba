package jp.inaba.identity.service.presentation.user.get

import jp.inaba.common.presentation.shared.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(assignableTypes = [GetUserController::class])
class GetUserControllerAdvice {
    @ExceptionHandler(UserNotFoundException::class)
    fun handle(ex: UserNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(ex.message),
            HttpStatus.NOT_FOUND
        )
    }
}