package jp.inaba.service.application.saga.usersetup

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.message.auth.command.UpdateIdTokenAttributeForBasketIdCommand
import jp.inaba.message.auth.updateIdTokenAttributeForBasketId
import org.axonframework.commandhandling.gateway.CommandGateway

private val logger = KotlinLogging.logger {}

class UpdateIdTokenAttributeForBasketIdStep(
    private val commandGateway: CommandGateway,
) {
    fun handle(
        command: UpdateIdTokenAttributeForBasketIdCommand,
        onFail: () -> Unit,
    ) {
        try {
            commandGateway.updateIdTokenAttributeForBasketId(command)
        } catch (e: Exception) {
            logger.warn { "IdTokenの属性変更に失敗しました exception:[$e]" }
            onFail.invoke()
        }
    }
}
