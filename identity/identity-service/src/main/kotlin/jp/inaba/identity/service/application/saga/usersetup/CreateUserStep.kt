package jp.inaba.identity.service.application.saga.usersetup

import jp.inaba.identity.api.domain.user.UserCommands
import jp.inaba.identity.api.domain.user.createUser
import org.axonframework.commandhandling.gateway.CommandGateway

class CreateUserStep(
    private val commandGateway: CommandGateway
) {
    private var onSuccess: (() -> Unit)? = null
    private var onFail: ((Exception) -> Unit)? = null

    fun onSuccess(onSuccess: () -> Unit): CreateUserStep {
        this.onSuccess = onSuccess
        return this
    }

    fun onFail(onFail: (Exception) -> Unit): CreateUserStep {
        this.onFail = onFail
        return this
    }

    fun execute(command: UserCommands.Create) {
        try {
            commandGateway.createUser(command)
            onSuccess?.invoke()
        }
        catch(e: Exception) {
            onFail?.invoke(e)
        }
    }
}