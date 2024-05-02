package jp.inaba.identity.service.application.saga.usersetup

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.identity.api.domain.external.auth.AuthCommands
import jp.inaba.identity.api.domain.external.auth.deleteAuthUser
import org.axonframework.commandhandling.gateway.CommandGateway

class DeleteAuthUserStep(
    private val commandGateway: CommandGateway
) {
    private val logger = KotlinLogging.logger {}

    fun handle(
        command: AuthCommands.DeleteAuthUser,
        onFail: (() -> Unit)? = null
    ) {
        try {
            commandGateway.deleteAuthUser(command)
        }
        catch(e: Exception) {
            logger.error { "認証ユーザーの削除に失敗しました exception:[${e}]" }
            onFail?.invoke()
        }
    }
}