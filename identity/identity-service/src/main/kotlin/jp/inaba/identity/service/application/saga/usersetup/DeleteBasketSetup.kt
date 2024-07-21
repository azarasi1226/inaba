package jp.inaba.identity.service.application.saga.usersetup

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.basket.api.domain.basket.DeleteBasketCommand
import jp.inaba.basket.api.domain.basket.deleteBasket
import jp.inaba.identity.api.domain.external.auth.AuthCommands
import jp.inaba.identity.api.domain.external.auth.deleteAuthUser
import org.axonframework.commandhandling.gateway.CommandGateway

private val logger = KotlinLogging.logger {}

class DeleteBasketSetup(
    private val commandGateway: CommandGateway,
) {
    fun handle(
        command: DeleteBasketCommand,
        onFail: () -> Unit,
    ) {
        try {
            commandGateway.deleteBasket(command)
        } catch (e: Exception) {
            logger.warn { "買い物かごの削除に失敗しました exception:[$e]" }
            onFail.invoke()
        }
    }
}
