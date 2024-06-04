package jp.inaba.basket.service.presentation

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.common.domain.shared.DomainException
import jp.inaba.common.presentation.shared.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

private val logger = KotlinLogging.logger { }

@RestControllerAdvice
class CommonAdvice {
    @ExceptionHandler(DomainException::class)
    fun handle(ex: DomainException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    errorCode = ex.errorCode,
                    errorMessage = ex.errorMessage,
                ),
            )
    }

//    @ExceptionHandler
//    fun handle(ex: CommandExecutionException): ResponseEntity<ErrorResponse> {
//        val responseEntity =
//            when (ex.cause) {
//                is AxonServerNonTransientRemoteCommandHandlingException ->
//                    return ResponseEntity(
//                        ErrorResponse(ex.message),
//                        HttpStatus.NOT_FOUND,
//                )
//                else -> {
//                    ex.printStackTrace()
//
//                    ResponseEntity(ErrorResponse(ex.message), HttpStatus.INTERNAL_SERVER_ERROR)
//                }
//            }
//
//        return responseEntity
//    }

    @ExceptionHandler(Exception::class)
    fun handle(ex: Exception): ResponseEntity<ErrorResponse> {
        logger.error { "原因不明のエラーがー発生しました。" }

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse(
                    errorCode = "",
                    errorMessage = ex.message ?: "InternalServerError",
                ),
            )
    }
}
