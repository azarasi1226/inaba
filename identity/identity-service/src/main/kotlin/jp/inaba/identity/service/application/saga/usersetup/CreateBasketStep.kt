package jp.inaba.identity.service.application.saga.usersetup

import org.axonframework.commandhandling.gateway.CommandGateway

class CreateBasketStep(
    private val commandGateway: CommandGateway
) {

}