package jp.inaba.identity.api.domain.external.cognito

import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.commandhandling.RoutingKey

interface CognitoCommand {
    @get:RoutingKey
    val emailAddress: String
}

object CognitoCommands {
    data class Signup(
        override val emailAddress: String,
        val password: String
    ) : CognitoCommand

    data class ConfirmSignup(
        override val emailAddress: String,
        val confirmCode: String
    ) : CognitoCommand

    data class ResendConfirmCode(
        override val emailAddress: String
    ) : CognitoCommand

    data class UpdateCustomAttributeForUserId(
        override val emailAddress: String,
        val userId: UserId
    ) : CognitoCommand
}