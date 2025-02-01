package jp.inaba.service.config.interceptor

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.core.domain.common.UseCaseError
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.service.utlis.getWrapUseCaseError
import jp.inaba.service.utlis.isWrapUseCaseError
import org.axonframework.commandhandling.CommandExecutionException
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.messaging.InterceptorChain
import org.axonframework.messaging.MessageHandlerInterceptor
import org.axonframework.messaging.unitofwork.UnitOfWork

private val logger = KotlinLogging.logger {}

class ExceptionWrappingCommandHandlerInterceptor : MessageHandlerInterceptor<CommandMessage<*>> {
    // https://discuss.axoniq.io/t/interceptorchain-proceed-must-not-be-null/4908
    // 戻り値はnullを許容しなくてはいけない。
    override fun handle(unitOfWork: UnitOfWork<out CommandMessage<*>>, interceptorChain: InterceptorChain): Any? {
        try {
            return interceptorChain.proceed()
        } catch (e: Throwable) {
            throw CommandExecutionException(
                e.message, e, exceptionDetails(e)
            )
        }
    }

    private fun exceptionDetails(e: Throwable): UseCaseError {
        //TODO("リトライ可能例外が出た場合は詰め替える処理を書いたほうがいいかも")
        return when (e) {
            // CommandHandler → 呼び出し元
            is UseCaseException -> {
                e.error
            }
            // CommandHandler → CommandHandler(n)　→　呼び出し元
            is CommandExecutionException -> {
                if (e.isWrapUseCaseError()) {
                    logger.warn { "CommandHandlerからの例外を受け取りましたが、Detailsが取得できませんでした。" }
                    throw e
                }

                e.getWrapUseCaseError()
            }
            // 謎...この子はいったいどこからきたの...
            else -> {
                logger.warn { "CommandHandlerで想定外の例外がthrowされました。" }
                throw e
            }
        }
    }
}