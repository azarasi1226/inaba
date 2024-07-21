package jp.inaba.identity.api.domain.external.auth

import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.identity.api.domain.user.UserId

interface AuthEvent {
    val emailAddress: String
}

data class SignedupEvent(
    override val emailAddress: String,
    val password: String,
) : AuthEvent

data class SignupConfirmedEvent(
    override val emailAddress: String,
    val confirmCode: String,
) : AuthEvent

data class ConfirmCodeResentEvent(
    override val emailAddress: String,
) : AuthEvent

data class IdTokenAttributeForUserIdUpdatedEvent(
    override val emailAddress: String,
    val userId: UserId,
) : AuthEvent

data class IdTokenAttributeForBasketIdUpdatedEvent(
    override val emailAddress: String,
    val basketId: BasketId,
) : AuthEvent

data class AuthUserDeletedEvent(
    override val emailAddress: String,
) : AuthEvent