package jp.inaba.service2.feature

import io.github.oshai.kotlinlogging.KotlinLogging
import io.grpc.Metadata
import io.grpc.Status
import io.grpc.StatusException
import jp.inaba.core.domain.common.ValueObjectException
import jp.inaba.message.UseCaseException
import net.devh.boot.grpc.server.advice.GrpcAdvice
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler

private val logger = KotlinLogging.logger {}

@GrpcAdvice
class GrpcServiceAdvice {
    @GrpcExceptionHandler
    fun handleDomainException(e: ValueObjectException): StatusException {
        logger.warn { "handle DomainException:[${e.errorMessage}]" }
        val status = Status.INVALID_ARGUMENT.withDescription(e.errorMessage).withCause(e)

        val metadata =
            GrpcErrorDetails(
                errorType = "value-object-error",
                errorCode = "0",
                errorMessage = e.errorMessage,
            ).toMetadata()

        return status.asException(metadata)
    }

    @GrpcExceptionHandler
    fun handleCommandUseCaseException(e: UseCaseException): StatusException {
        logger.warn { "handle Command UseCaseException:[${e.error.message}]" }

        val status =
            Status.UNKNOWN
                .withDescription(e.error.message)
                .withCause(e)

        val metadata =
            GrpcErrorDetails(
                errorType = "usecase-error",
                errorCode = "",
                errorMessage = e.error.message,
            ).toMetadata()

        return status.asException(metadata)
    }

    @GrpcExceptionHandler
    fun handleUnknownException(e: Exception): StatusException {
        logger.error { "handle UnknownException" }
        logger.error { e.stackTraceToString() }
        val status = Status.INTERNAL.withDescription("想定外のエラーが発生しました。").withCause(e)

        val metadata =
            GrpcErrorDetails(
                errorType = "unknown-error",
                errorCode = "-1",
                errorMessage = "想定外のエラーが発生しました。",
            ).toMetadata()

        return status.asException(metadata)
    }
}

class GrpcErrorDetails(
    val errorType: String,
    val errorCode: String,
    val errorMessage: String,
) {
    fun toMetadata(): Metadata {
        val errorTypeKey =
            Metadata.Key
                .of("error-type", Metadata.ASCII_STRING_MARSHALLER)
        val errorCodeKey =
            Metadata.Key
                .of("error-code", Metadata.ASCII_STRING_MARSHALLER)
        val errorMessageKey =
            Metadata.Key
                .of("error-message-bin", Metadata.BINARY_BYTE_MARSHALLER)

        return Metadata().apply {
            put(errorTypeKey, errorType)
            put(errorCodeKey, errorCode)
            put(errorMessageKey, errorMessage.toByteArray())
        }
    }
}
