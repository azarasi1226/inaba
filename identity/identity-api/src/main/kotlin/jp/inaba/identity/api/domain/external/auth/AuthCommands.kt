package jp.inaba.identity.api.domain.external.auth

import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.commandhandling.RoutingKey

interface AuthCommand {
    @get:RoutingKey
    val emailAddress: String
}

data class SignupCommand(
    override val emailAddress: String,
    val password: String,
) : AuthCommand

data class ConfirmSignupCommand(
    override val emailAddress: String,
    val confirmCode: String,
) : AuthCommand

data class ResendConfirmCodeCommand(
    override val emailAddress: String,
) : AuthCommand

data class UpdateIdTokenAttributeForUserIdCommand(
    override val emailAddress: String,
    val userId: UserId,
) : AuthCommand

data class UpdateIdTokenAttributeForBasketIdCommand(
    override val emailAddress: String,
    val basketId: BasketId,
) : AuthCommand

data class DeleteAuthUserCommand(
    override val emailAddress: String,
) : AuthCommand