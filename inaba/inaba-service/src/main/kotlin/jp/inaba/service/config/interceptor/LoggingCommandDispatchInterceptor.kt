package jp.inaba.service.config.interceptor

import io.github.oshai.kotlinlogging.KotlinLogging
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.messaging.MessageDispatchInterceptor
import java.util.function.BiFunction

private val logger = KotlinLogging.logger {}

class LoggingCommandDispatchInterceptor : MessageDispatchInterceptor<CommandMessage<*>> {
    override fun handle(messages: List<CommandMessage<*>>): BiFunction<Int, CommandMessage<*>, CommandMessage<*>> {
        return BiFunction { _, command ->
            // TODO:もっときれいな表示形式にしたいね('ω')
            logger.debug { "コマンド発行：${command.payload}" }

            command
        }
    }
}