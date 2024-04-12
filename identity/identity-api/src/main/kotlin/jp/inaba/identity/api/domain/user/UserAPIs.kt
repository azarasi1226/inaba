package jp.inaba.identity.api.domain.user

import org.axonframework.commandhandling.gateway.CommandGateway

fun CommandGateway.createUser(command: UserCommands.Create) {
    this.sendAndWait<Any>(command)
}