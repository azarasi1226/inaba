package jp.inaba.identity.service.application.saga.usersetup

import jp.inaba.identity.api.domain.user.UserCommands
import jp.inaba.identity.api.domain.user.deleteUser
import org.axonframework.commandhandling.gateway.CommandGateway

class DeleteUserStep(
    private val commandGateway: CommandGateway
) {
    private var onSuccess: (() -> Unit)? = null
    private var onFail: ((Exception) -> Unit)? = null

    fun onSuccess(onSuccess: () -> Unit): DeleteUserStep {
        this.onSuccess = onSuccess
        return this
    }

    fun onFail(onFail: (Exception) -> Unit): DeleteUserStep {
        this.onFail = onFail
        return this
    }

    fun execute(command: UserCommands.Delete) {
        try {
            commandGateway.deleteUser(command)
            onSuccess?.invoke()
        }
        catch(e: Exception) {
            onFail?.invoke(e)
        }
    }
}