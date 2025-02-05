package jp.inaba.service.application.saga

import io.github.oshai.kotlinlogging.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import kotlin.reflect.KClass

private val logger = KotlinLogging.logger {}

class SagaStep<C : Any>(
    private val commandGateway: CommandGateway,
    private val commandClass: KClass<C>
) {
    fun handle(
        command: C,
        onFail: () -> Unit,
    ) {
        try {
            commandGateway.sendAndWait<Any>(command)
        } catch (e: Exception) {
            logger.error { "command:[${commandClass.simpleName}]が失敗しました。" }
            logger.error { "reason :[${e.message}]"}
            logger.error { "補償トランザクションを開始します。" }
            onFail.invoke()
        }
    }
}
