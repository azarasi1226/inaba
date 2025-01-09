package jp.inaba.service.application.saga.usersetup

import io.github.oshai.kotlinlogging.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import jp.inaba.message.basket.command.CreateBasketCommand
import jp.inaba.message.basket.createBasket

private val logger = KotlinLogging.logger {}

class CreateBasketStep(
    private val commandGateway: CommandGateway,
) {
    fun handle(
        command: CreateBasketCommand,
        onFail: () -> Unit,
    ) {
        try {
            val result = commandGateway.createBasket(command)

            if (result.isErr) {
                logger.warn { "買い物かご作成に失敗しました error:[${result.error}]" }
                onFail.invoke()
            }
        } catch (e: Exception) {
            logger.warn { "買い物かご作成に失敗しました exception:[$e]" }
            onFail.invoke()
        }
    }
}
