package jp.inaba.message.auth

import jp.inaba.message.auth.command.ConfirmSignupCommand
import jp.inaba.message.auth.command.DeleteAuthUserCommand
import jp.inaba.message.auth.command.ResendConfirmCodeCommand
import jp.inaba.message.auth.command.SignupCommand
import jp.inaba.message.auth.command.UpdateIdTokenAttributeForBasketIdCommand
import jp.inaba.message.auth.command.UpdateIdTokenAttributeForUserIdCommand
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
