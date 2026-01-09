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
    @GrpcExceptionHandler
    fun handleDomainException(e: ValueObjectException): StatusException {
        logger.warn { "handle DomainException:[${e.errorMessage}]" }

        val status = Status.INVALID_ARGUMENT.withDescription(e.errorMessage).withCause(e)

        val errorTypeKey = Metadata.Key.of("error-type", Metadata.ASCII_STRING_MARSHALLER)
        val errorCodeKey = Metadata.Key.of("error-code", Metadata.ASCII_STRING_MARSHALLER)
        val errorMessageKey = Metadata.Key.of("error-message", Metadata.ASCII_STRING_MARSHALLER)
        val metadata =
            Metadata().apply {
                put(errorTypeKey, "inaba")
                put(errorCodeKey, 0.toString()) //Valueのエラーの際のエラーコードを考えるべき
                put(errorMessageKey, e.errorMessage)
            }

        return status.asException(metadata)
    }

    @GrpcExceptionHandler
    fun handleCommandUseCaseException(e: CommandExecutionException): StatusException {
        if (e.isWrapUseCaseError()) {
            val error = e.getWrapUseCaseError()
            logger.warn { "handle Command UseCaseException:[${error.errorMessage}]" }

            val status =
                Status.INVALID_ARGUMENT
                    .withDescription(error.errorMessage)
                    .withCause(e)

            val errorTypeKey = Metadata.Key.of("error-type", Metadata.ASCII_STRING_MARSHALLER)
            val errorCodeKey = Metadata.Key.of("error-code", Metadata.ASCII_STRING_MARSHALLER)
            val errorMessageKey = Metadata.Key.of("error-message", Metadata.ASCII_STRING_MARSHALLER)
            val metadata =
                Metadata().apply {
                    put(errorTypeKey, "inaba")
                    put(errorCodeKey, error.errorCode)
                    put(errorMessageKey, error.errorMessage)
                }

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

                val errorTypeKey = Metadata.Key.of("error-type", Metadata.ASCII_STRING_MARSHALLER)
                val errorCodeKey = Metadata.Key.of("error-code", Metadata.ASCII_STRING_MARSHALLER)
                val errorMessageKey = Metadata.Key.of("error-message", Metadata.ASCII_STRING_MARSHALLER)
                val metadata =
                    Metadata().apply {
                        put(errorTypeKey, "inaba")
                        put(errorCodeKey, error.errorCode)
                        put(errorMessageKey, error.errorMessage)
                    }

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

        val errorTypeKey = Metadata.Key.of("error-type", Metadata.ASCII_STRING_MARSHALLER)
        val errorMessageKey = Metadata.Key.of("error-message", Metadata.ASCII_STRING_MARSHALLER)
        val metadata =
            Metadata().apply {
                put(errorTypeKey, "unknown")
                put(errorMessageKey, e.message)
            }

        return status.asException(metadata)
    }
}
