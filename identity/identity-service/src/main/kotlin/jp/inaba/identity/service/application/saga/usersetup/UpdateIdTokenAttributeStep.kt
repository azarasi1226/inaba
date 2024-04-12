package jp.inaba.identity.service.application.saga.usersetup

import jp.inaba.identity.api.domain.external.auth.AuthCommands
import jp.inaba.identity.api.domain.external.auth.updateIdTokenAttribute
import org.axonframework.commandhandling.gateway.CommandGateway

class UpdateIdTokenAttributeStep(
    private val commandGateway: CommandGateway
) {
    private var onSuccess: (() -> Unit)? = null
    private var onFail: ((Exception) -> Unit)? = null

    fun onSuccess(onSuccess: () -> Unit): UpdateIdTokenAttributeStep {
        this.onSuccess = onSuccess
        return this
    }

    fun onFail(onFail: (Exception) -> Unit): UpdateIdTokenAttributeStep {
        this.onFail = onFail
        return this
    }

    fun execute(command: AuthCommands.UpdateIdTokenAttribute) {
        try {
            commandGateway.updateIdTokenAttribute(command)
            onSuccess?.invoke()
        }
        catch(e: Exception) {
            onFail?.invoke(e)
        }
    }
}