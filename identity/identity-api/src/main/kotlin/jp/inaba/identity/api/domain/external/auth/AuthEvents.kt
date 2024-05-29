package jp.inaba.identity.api.domain.external.auth

import jp.inaba.identity.api.domain.user.UserId


interface AuthEvent {
    val emailAddress: String
}

object AuthEvents {
    data class Signedup(
        override val emailAddress: String,
        val password: String
    ) : AuthEvent

    data class SignupConfirmed(
        override val emailAddress: String,
        val confirmCode: String
    ) : AuthEvent

    data class ConfirmCodeResent(
        override val emailAddress: String
    ) : AuthEvent

    data class IdTokenAttributeForUserIdUpdated(
        override val emailAddress: String,
        val userId: UserId
    ) : AuthEvent

    data class AuthUserDeleted(
        override val emailAddress: String
    ) : AuthEvent
}