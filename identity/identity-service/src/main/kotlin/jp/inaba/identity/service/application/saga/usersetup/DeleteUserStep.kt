package jp.inaba.identity.service.application.saga.usersetup

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.identity.api.domain.user.UserCommands
import jp.inaba.identity.api.domain.user.deleteUser
import org.axonframework.commandhandling.gateway.CommandGateway

class DeleteUserStep(
    private val commandGateway: CommandGateway
) {
    private val logger = KotlinLogging.logger {}

    fun handle(
        command: UserCommands.Delete,
        onFail: (() -> Unit)? = null
    ) {
        try {
            commandGateway.deleteUser(command)
        }
        catch(e: Exception) {
            logger.error { "ユーザーの削除に失敗しました exception:[${e}]" }
            onFail?.invoke()
        }
    }
}