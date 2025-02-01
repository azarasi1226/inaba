package jp.inaba.service.presentation

import io.github.oshai.kotlinlogging.KotlinLogging
import io.grpc.Status
import jp.inaba.core.domain.common.DomainException
import jp.inaba.service.utlis.getWrapUseCaseError
import jp.inaba.service.utlis.isWrapUseCaseError
import net.devh.boot.grpc.server.advice.GrpcAdvice
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler
import org.axonframework.commandhandling.CommandExecutionException
import org.axonframework.queryhandling.QueryExecutionException
import java.util.concurrent.ExecutionException

private val logger = KotlinLogging.logger {}

@GrpcAdvice
class GrpcExceptionAdvice {
    @GrpcExceptionHandler
    fun handleDomainException(e: DomainException) : Status {
        logger.warn { "handle DomainException" }
        return Status.INVALID_ARGUMENT.withDescription(e.errorMessage).withCause(e)
    }

    @GrpcExceptionHandler
    fun handleCommandUseCaseException(e: CommandExecutionException) : Status {
        if(e.isWrapUseCaseError()) {
            logger.warn { "handle Command UseCaseException" }
            val error = e.getWrapUseCaseError()
            return Status.INVALID_ARGUMENT.withDescription(error.errorMessage).withCause(e)
        }

        return handleUnknownException(e)
    }

    // TODO: CompletableFutureの中で発生する例外はExecutionExceptionにラップされて元の例外がわからないので一回判定を挟んでいる。
    // そのままの例外をキャッチできるようにQueryの問い合わせの部分工夫できないかな？
    @GrpcExceptionHandler
    fun handleQueryUseCaseException(e: ExecutionException) : Status {
        if (e.cause is QueryExecutionException){
            val exception = e.cause as QueryExecutionException

            if(exception.isWrapUseCaseError()) {
                logger.warn { "handle QueryUseCaseException" }
                val error = exception.getWrapUseCaseError()
                return Status.INVALID_ARGUMENT.withDescription(error.errorMessage).withCause(e)
            }
        }

        return handleUnknownException(e)
    }

    @GrpcExceptionHandler
    fun handleUnknownException(e: Exception) : Status {
        logger.error { "handle UnknownException" }
        logger.error { e.stackTraceToString() }
        return Status.INTERNAL.withDescription("想定外のエラーが発生しました。").withCause(e)
    }
}