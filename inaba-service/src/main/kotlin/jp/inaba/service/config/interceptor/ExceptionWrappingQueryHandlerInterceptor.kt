package jp.inaba.service.config.interceptor

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.core.domain.common.UseCaseError
import jp.inaba.core.domain.common.UseCaseException
import org.axonframework.messaging.InterceptorChain
import org.axonframework.messaging.MessageHandlerInterceptor
import org.axonframework.messaging.unitofwork.UnitOfWork
import org.axonframework.queryhandling.QueryExecutionException
import org.axonframework.queryhandling.QueryMessage

private val logger = KotlinLogging.logger {}

class ExceptionWrappingQueryHandlerInterceptor : MessageHandlerInterceptor<QueryMessage<*, *>> {
    // https://discuss.axoniq.io/t/interceptorchain-proceed-must-not-be-null/4908
    // 戻り値はnullを許容しなくてはいけない。
    override fun handle(
        unitOfWork: UnitOfWork<out QueryMessage<*, *>>,
        interceptorChain: InterceptorChain,
    ): Any? {
        try {
            return interceptorChain.proceed()
        } catch (e: Throwable) {
            throw QueryExecutionException(
                e.message,
                e,
                exceptionDetails(e),
            )
        }
    }

    private fun exceptionDetails(e: Throwable): UseCaseError =
        when (e) {
            // QueryHandler → 呼び出し元
            is UseCaseException -> {
                e.error
            }
            // 謎...この子はいったいどこからきたの...
            else -> {
                logger.warn { "QueryHandlerで想定外の例外がthrowされました。" }
                throw e
            }
        }
}
