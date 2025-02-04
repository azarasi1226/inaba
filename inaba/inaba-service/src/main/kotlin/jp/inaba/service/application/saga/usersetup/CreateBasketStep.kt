package jp.inaba.service.application.saga.usersetup

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.message.basket.command.CreateBasketCommand
import org.axonframework.commandhandling.gateway.CommandGateway

private val logger = KotlinLogging.logger {}

class CreateBasketStep(
    private val commandGateway: CommandGateway,
) {
    fun handle(
        command: CreateBasketCommand,
        onFail: () -> Unit,
    ) {
        try {
            commandGateway.sendAndWait<Any>(command)
        } catch (e: Exception) {
            logger.error { "買い物かご作成に失敗しました exception:[$e]" }
            onFail.invoke()
        }
    }
}
