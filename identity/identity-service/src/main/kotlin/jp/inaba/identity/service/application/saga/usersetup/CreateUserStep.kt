package jp.inaba.identity.service.application.saga.usersetup

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.identity.api.domain.user.UserCommands
import jp.inaba.identity.api.domain.user.createUser
import org.axonframework.commandhandling.gateway.CommandGateway

class CreateUserStep(
    private val commandGateway: CommandGateway
) {
    private val logger = KotlinLogging.logger {}

    fun handle(
        command: UserCommands.Create,
        onFail: (() -> Unit)? = null
    ) {
        try {
            commandGateway.createUser(command)
        }
        catch(e: Exception) {
            logger.error { "ユーザー作成に失敗しました exception:[${e}]" }
            onFail?.invoke()
        }
    }
}