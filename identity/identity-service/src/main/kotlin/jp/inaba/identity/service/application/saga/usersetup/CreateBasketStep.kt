package jp.inaba.identity.service.application.saga.usersetup

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.api.domain.basket.createBasket
import org.axonframework.commandhandling.gateway.CommandGateway

class CreateBasketStep(
    private val commandGateway: CommandGateway
) {
    private val logger = KotlinLogging.logger {}

    fun handle(
        command: BasketCommands.Create,
        onFail: (() -> Unit)? = null
    ) {
        try {
            val result = commandGateway.createBasket(command)

            if(result.isErr) {
                logger.error { "バスケット作成に失敗しました error:[${result.error}]" }
                onFail?.invoke()
            }
        }
        catch(e: Exception) {
            logger.error { "バスケット作成に失敗しました exception:[${e}]" }
            onFail?.invoke()
        }
    }
}