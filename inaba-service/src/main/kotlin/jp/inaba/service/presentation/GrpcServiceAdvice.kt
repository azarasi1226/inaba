package jp.inaba.service.presentation

import io.github.oshai.kotlinlogging.KotlinLogging
import io.grpc.Metadata
import io.grpc.Status
import io.grpc.StatusException
import jp.inaba.core.domain.common.ValueObjectException
import jp.inaba.service.utlis.getWrapUseCaseError
import jp.inaba.service.utlis.isWrapUseCaseError
import net.devh.boot.grpc.server.advice.GrpcAdvice
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler
import org.axonframework.commandhandling.CommandExecutionException
import org.axonframework.queryhandling.QueryExecutionException
import java.util.concurrent.ExecutionException

private val logger = KotlinLogging.logger {}

// TODO(全体的に美しくない。作り直したい。)
@GrpcAdvice
class GrpcServiceAdvice {
    // まだCommand/Queryを投げる前のPresentation層で発生する例外の想定
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

    // Commandが投げられた後のUseCase層 / Domain層で発生する例外の想定
    // CommandExecutionExceptionの中にラップされているので、取り出す必要がある。
    @GrpcExceptionHandler
    fun handleCommandUseCaseException(e: CommandExecutionException): StatusException {
        if (e.isWrapUseCaseError()) {
            val error = e.getWrapUseCaseError()
            logger.warn { "handle Command UseCaseException:[${error.errorMessage}]" }

            val status =
                Status.UNKNOWN
                    .withDescription(error.errorMessage)
                    .withCause(e)

            val metadata =
                GrpcErrorDetails(
                    errorType = "usecase-error",
                    errorCode = error.errorCode,
                    errorMessage = error.errorMessage,
                ).toMetadata()

            return status.asException(metadata)
        }

        return handleUnknownException(e)
    }

    // CompletableFutureの中で発生する例外はExecutionExceptionにラップされて元の例外がわからないので一回判定を挟んでいる。
    // TODO:そのままの例外をキャッチできるようにQueryの問い合わせの部分工夫できないかな？
    @GrpcExceptionHandler
    fun handleQueryUseCaseException(e: ExecutionException): StatusException {
        if (e.cause is QueryExecutionException) {
            val exception = e.cause as QueryExecutionException

            if (exception.isWrapUseCaseError()) {
                val error = exception.getWrapUseCaseError()
                logger.warn { "handle QueryUseCaseException:[${error.errorMessage}]" }

                val status =
                    Status.INVALID_ARGUMENT
                        .withDescription(error.errorMessage)
                        .withCause(e)

                val metadata =
                    GrpcErrorDetails(
                        errorType = "usecase-error",
                        errorCode = error.errorCode,
                        errorMessage = error.errorMessage,
                    ).toMetadata()

                return status.asException(metadata)
            }
        }

        return handleUnknownException(e)
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
        val errorTypeKey = Metadata.Key.of("error-type", Metadata.ASCII_STRING_MARSHALLER)
        val errorCodeKey = Metadata.Key.of("error-code", Metadata.ASCII_STRING_MARSHALLER)
        val errorMessageKey = Metadata.Key.of("error-message-bin", Metadata.BINARY_BYTE_MARSHALLER)

        return Metadata().apply {
            put(errorTypeKey, errorType)
            put(errorCodeKey, errorCode)
            put(errorMessageKey, errorMessage.toByteArray())
        }
    }
}
