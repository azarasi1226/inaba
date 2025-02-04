package jp.inaba.service.application.saga.usersetup

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.message.user.command.LinkBasketIdCommand
import org.axonframework.commandhandling.gateway.CommandGateway

private val logger = KotlinLogging.logger {}

class LinkBasketIdStep(
    private val commandGateway: CommandGateway
) {
    fun handle(
        command: LinkBasketIdCommand,
        onFail: () -> Unit,
    ) {
        try {
            commandGateway.sendAndWait<Any>(command)
        } catch(e: Exception) {
            logger.error { "BasketIdの紐づけに失敗しました exception:[$e]" }
            onFail.invoke()
        }
    }
}