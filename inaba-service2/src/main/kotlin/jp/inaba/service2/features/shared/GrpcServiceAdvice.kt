package jp.inaba.service2.features.shared

import io.github.oshai.kotlinlogging.KotlinLogging
import io.grpc.Metadata
import io.grpc.Status
import io.grpc.StatusException
import jp.inaba.core.domain.common.ValueObjectException
import jp.inaba.message.UseCaseException
import net.devh.boot.grpc.server.advice.GrpcAdvice
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler

private val logger = KotlinLogging.logger {}

// TODO(全体的に美しくない。作り直したい。)
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
    fun toMetadata(): io.grpc.Metadata {
        val errorTypeKey =
            io.grpc.Metadata.Key
                .of("error-type", io.grpc.Metadata.ASCII_STRING_MARSHALLER)
        val errorCodeKey =
            io.grpc.Metadata.Key
                .of("error-code", io.grpc.Metadata.ASCII_STRING_MARSHALLER)
        val errorMessageKey =
            io.grpc.Metadata.Key
                .of("error-message-bin", io.grpc.Metadata.BINARY_BYTE_MARSHALLER)

        return Metadata().apply {
            put(errorTypeKey, errorType)
            put(errorCodeKey, errorCode)
            put(errorMessageKey, errorMessage.toByteArray())
        }
    }
}
