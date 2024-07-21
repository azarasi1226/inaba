package jp.inaba.identity.service.application.saga.usersetup

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.identity.api.domain.external.auth.DeleteAuthUserCommand
import jp.inaba.identity.api.domain.external.auth.deleteAuthUser
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
