package jp.inaba.apigateway

import io.grpc.Status
import io.grpc.StatusRuntimeException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerAdvice {
    @ExceptionHandler(StatusRuntimeException::class)
    fun handle(e: StatusRuntimeException): ResponseEntity<Any> {
        return if (e.status.code == Status.INVALID_ARGUMENT.code) {
            ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.message)
        } else {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("原因不明")
        }
    }
}
