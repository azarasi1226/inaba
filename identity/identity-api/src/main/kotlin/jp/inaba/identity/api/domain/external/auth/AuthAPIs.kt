package jp.inaba.identity.api.domain.external.auth

import jp.inaba.identity.api.domain.external.auth.command.ConfirmSignupCommand
import jp.inaba.identity.api.domain.external.auth.command.DeleteAuthUserCommand
import jp.inaba.identity.api.domain.external.auth.command.ResendConfirmCodeCommand
import jp.inaba.identity.api.domain.external.auth.command.SignupCommand
import jp.inaba.identity.api.domain.external.auth.command.UpdateIdTokenAttributeForBasketIdCommand
import jp.inaba.identity.api.domain.external.auth.command.UpdateIdTokenAttributeForUserIdCommand
import org.axonframework.commandhandling.gateway.CommandGateway

fun CommandGateway.signup(command: SignupCommand) {
    this.sendAndWait<Any>(command)
}

fun CommandGateway.confirmSignup(command: ConfirmSignupCommand) {
    this.sendAndWait<Any>(command)
}

fun CommandGateway.resendConfirmCode(command: ResendConfirmCodeCommand) {
    this.sendAndWait<Any>(command)
}

fun CommandGateway.updateIdTokenAttributeForUserId(command: UpdateIdTokenAttributeForUserIdCommand) {
    this.sendAndWait<Any>(command)
}

fun CommandGateway.updateIdTokenAttributeForBasketId(command: UpdateIdTokenAttributeForBasketIdCommand) {
    this.sendAndWait<Any>(command)
}

fun CommandGateway.deleteAuthUser(command: DeleteAuthUserCommand) {
    this.sendAndWait<Any>(command)
}
