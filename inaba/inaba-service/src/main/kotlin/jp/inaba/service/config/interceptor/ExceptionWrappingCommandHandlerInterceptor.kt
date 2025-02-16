package jp.inaba.service.config.interceptor

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.core.domain.common.CommonError
import jp.inaba.core.domain.common.UseCaseError
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.service.utlis.getWrapUseCaseError
import jp.inaba.service.utlis.isWrapUseCaseError
import org.axonframework.commandhandling.CommandExecutionException
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.eventsourcing.AggregateDeletedException
import org.axonframework.eventsourcing.IncompatibleAggregateException
import org.axonframework.messaging.InterceptorChain
import org.axonframework.messaging.MessageHandlerInterceptor
import org.axonframework.messaging.unitofwork.UnitOfWork
import org.axonframework.modelling.command.AggregateNotFoundException

private val logger = KotlinLogging.logger {}

class ExceptionWrappingCommandHandlerInterceptor : MessageHandlerInterceptor<CommandMessage<*>> {
    // https://discuss.axoniq.io/t/interceptorchain-proceed-must-not-be-null/4908
    // 戻り値はnullを許容しなくてはいけない。
    override fun handle(
        unitOfWork: UnitOfWork<out CommandMessage<*>>,
        interceptorChain: InterceptorChain,
    ): Any? {
        try {
            return interceptorChain.proceed()
        } catch (e: Throwable) {
            throw CommandExecutionException(
                e.message,
                e,
                exceptionDetails(e),
            )
        }
    }

    private fun exceptionDetails(e: Throwable): UseCaseError {
        // 呼び出し元 → CommandHandler(例外発生)
        // 直接Aggregateが呼ばれたり、外部CommandHandlerの場合はこのルート
        if (e is UseCaseException) {
            return e.error
        }

        // 呼び出し元 → CommandHandler→ CommandHandler(例外発生)
        // 一回CommandHandlerを経由してAggregateなどが呼ばれた場合はこのルート
        if(e is CommandExecutionException) {
            if (e.isWrapUseCaseError()) {
                return e.getWrapUseCaseError()
            }

            // 原因不明のエラー
            logger.warn { e::class.simpleName}
            logger.warn { "CommandHandlerで想定外の例外がthrowされました。" }
            throw e
        }

        // 共通エラー
        if (e is AggregateDeletedException) {
            return CommonError.AGGREGATE_DELETED
        }
        if(e is AggregateNotFoundException) {
            return CommonError.AGGREGATE_NOT_FOUND
        }
        if(e is IncompatibleAggregateException) {
            return CommonError.AGGREGATE_INCOMPATIBLE
        }

        // 原因不明のエラー
        logger.warn { e::class.simpleName}
        logger.warn { "CommandHandlerで想定外の例外がthrowされました。" }
        throw e
    }
}
