package jp.inaba.service.config.interceptor

import io.github.oshai.kotlinlogging.KotlinLogging
import org.axonframework.messaging.MessageDispatchInterceptor
import org.axonframework.queryhandling.QueryMessage
import java.util.function.BiFunction

private val logger = KotlinLogging.logger {}

class LoggingQueryDispatchInterceptor : MessageDispatchInterceptor<QueryMessage<*, *>> {
    override fun handle(messages: MutableList<out QueryMessage<*, *>>): BiFunction<Int, QueryMessage<*, *>, QueryMessage<*, *>> {
        return BiFunction { _, query ->
            // TODO:もっときれいな表示形式にしたいね('ω')
            logger.debug { "クエリ発行：${query.payload}" }

            query
        }
    }
}
