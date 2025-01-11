package jp.inaba.service.application.saga.usersetup

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.message.user.command.DeleteUserCommand
import jp.inaba.message.user.deleteUser
import org.axonframework.commandhandling.gateway.CommandGateway

private val logger = KotlinLogging.logger {}

class DeleteUserStep(
    private val commandGateway: CommandGateway,
) {
    fun handle(
        command: DeleteUserCommand,
        onFail: () -> Unit,
    ) {
        try {
            commandGateway.deleteUser(command)
        } catch (e: Exception) {
            logger.warn { "ユーザーの削除に失敗しました exception:[$e]" }
            onFail.invoke()
        }
    }
}
