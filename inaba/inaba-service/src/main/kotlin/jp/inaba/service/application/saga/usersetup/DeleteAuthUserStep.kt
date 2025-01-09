package jp.inaba.service.application.saga.usersetup

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.message.auth.command.DeleteAuthUserCommand
import jp.inaba.message.auth.deleteAuthUser
import org.axonframework.commandhandling.gateway.CommandGateway

private val logger = KotlinLogging.logger {}

class DeleteAuthUserStep(
    private val commandGateway: CommandGateway,
) {
    fun handle(
        command: DeleteAuthUserCommand,
        onFail: () -> Unit,
    ) {
        try {
            commandGateway.deleteAuthUser(command)
        } catch (e: Exception) {
            logger.warn { "認証ユーザーの削除に失敗しました exception:[$e]" }
            onFail.invoke()
        }
    }
}
