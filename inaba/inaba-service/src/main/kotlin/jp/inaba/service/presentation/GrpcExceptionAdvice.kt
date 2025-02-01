package jp.inaba.service.presentation

import io.github.oshai.kotlinlogging.KotlinLogging
import io.grpc.Status
import jp.inaba.core.domain.common.DomainException
import jp.inaba.core.domain.common.UseCaseError
import jp.inaba.service.utlis.getWrapUseCaseError
import jp.inaba.service.utlis.isWrapUseCaseError
import net.devh.boot.grpc.server.advice.GrpcAdvice
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler
import org.axonframework.commandhandling.CommandExecutionException
import org.axonframework.queryhandling.QueryExecutionException
import kotlin.jvm.optionals.getOrElse

private val logger = KotlinLogging.logger {}

@GrpcAdvice
class GrpcExceptionAdvice {
    @GrpcExceptionHandler
    fun handleDomainException(e: DomainException) : Status {
        return Status.INVALID_ARGUMENT.withDescription(e.errorMessage).withCause(e)
    }

    @GrpcExceptionHandler
    fun handleUseCaseException(e: CommandExecutionException) : Status {
        if(e.isWrapUseCaseError()) {
            val error = e.getWrapUseCaseError()
            return Status.INVALID_ARGUMENT.withDescription(error.errorMessage).withCause(e)
        }

        return handleUnknownException(e)
    }

    @GrpcExceptionHandler
    fun handleUseCaseException(e: QueryExecutionException) : Status {
        if(e.isWrapUseCaseError()) {
            val error = e.getWrapUseCaseError()
            return Status.INVALID_ARGUMENT.withDescription(error.errorMessage).withCause(e)
        }

        return handleUnknownException(e)
    }

    @GrpcExceptionHandler
    fun handleUnknownException(e: Exception) : Status {
        logger.error { "想定外のエラーが発生しました。" }
        logger.error { e.stackTraceToString() }
        return Status.INTERNAL.withDescription("想定外のエラーが発生しました。").withCause(e)
    }
}