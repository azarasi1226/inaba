package jp.inaba.identity.service.application.saga.usersetup

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.basket.api.domain.basket.command.CreateBasketCommand
import jp.inaba.basket.api.domain.basket.createBasket
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
