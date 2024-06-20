package jp.inaba.catalog.service.presentation

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.common.domain.shared.DomainException
import jp.inaba.common.presentation.shared.ErrorResponse
import org.axonframework.axonserver.connector.command.AxonServerNonTransientRemoteCommandHandlingException
import org.axonframework.commandhandling.CommandExecutionException
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

    @ExceptionHandler
    fun handle(ex: CommandExecutionException): ResponseEntity<ErrorResponse> {
        val responseEntity =
            when (ex.cause) {
                is AxonServerNonTransientRemoteCommandHandlingException ->
                    ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(
                            ErrorResponse(
                                errorCode = "",
                                errorMessage = "CommandHandlerNotFound",
                            ),
                        )
                else -> {
                    logger.error { "Command実行中に原因不明のエラーが発生しました。" }

                    ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(
                            ErrorResponse(
                                errorCode = "",
                                errorMessage = ex.message ?: "CommandHandlerException",
                            ),
                        )
                }
            }

        return responseEntity
    }

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
