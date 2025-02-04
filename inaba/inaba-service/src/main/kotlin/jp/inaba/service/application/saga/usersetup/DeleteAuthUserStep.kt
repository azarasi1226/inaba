package jp.inaba.service.application.saga.usersetup

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.message.auth.command.DeleteAuthUserCommand
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
            commandGateway.sendAndWait<Any>(command)
        } catch (e: Exception) {
            logger.error { "認証ユーザーの削除に失敗しました exception:[$e]" }
            onFail.invoke()
        }
    }
}
