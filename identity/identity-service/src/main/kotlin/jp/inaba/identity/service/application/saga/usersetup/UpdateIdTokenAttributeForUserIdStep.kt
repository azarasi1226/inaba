package jp.inaba.identity.service.application.saga.usersetup

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.identity.api.domain.external.auth.command.UpdateIdTokenAttributeForUserIdCommand
import jp.inaba.identity.api.domain.external.auth.updateIdTokenAttributeForUserId
import org.axonframework.commandhandling.gateway.CommandGateway

private val logger = KotlinLogging.logger {}

class UpdateIdTokenAttributeForUserIdStep(
    private val commandGateway: CommandGateway,
) {
    fun handle(
        command: UpdateIdTokenAttributeForUserIdCommand,
        onFail: () -> Unit,
    ) {
        try {
            commandGateway.updateIdTokenAttributeForUserId(command)
        } catch (e: Exception) {
            logger.warn { "IdTokenの属性変更に失敗しました exception:[$e]" }
            onFail.invoke()
        }
    }
}
