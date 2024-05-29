package jp.inaba.identity.api.domain.external.auth

import org.axonframework.commandhandling.gateway.CommandGateway

fun CommandGateway.signup(command: AuthCommands.Signup) {
    this.sendAndWait<Any>(command)
}

fun CommandGateway.confirmSignup(command: AuthCommands.ConfirmSignup) {
    this.sendAndWait<Any>(command)
}

fun CommandGateway.resendConfirmCode(command: AuthCommands.ResendConfirmCode) {
    this.sendAndWait<Any>(command)
}

fun CommandGateway.updateIdTokenAttributeForUserId(command: AuthCommands.UpdateIdTokenAttributeForUserId) {
    this.sendAndWait<Any>(command)
}

fun CommandGateway.deleteAuthUser(command: AuthCommands.DeleteAuthUser) {
    this.sendAndWait<Any>(command)
}