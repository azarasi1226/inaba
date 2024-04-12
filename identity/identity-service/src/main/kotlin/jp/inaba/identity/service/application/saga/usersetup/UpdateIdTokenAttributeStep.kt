package jp.inaba.identity.service.application.saga.usersetup

import org.axonframework.commandhandling.gateway.CommandGateway

class UpdateIdTokenAttributeStep(
    private val commandGateway: CommandGateway
) {

    private var onSuccess: (() -> Unit)? = null
    private var onFail: ((Exception) -> Unit)? = null
}