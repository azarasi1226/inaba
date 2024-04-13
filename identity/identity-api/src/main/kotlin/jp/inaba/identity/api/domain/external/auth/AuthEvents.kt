package jp.inaba.identity.api.domain.external.auth



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

    data class IdTokenAttributeUpdated(
        override val emailAddress: String,
        val idTokenAttributes: Map<String, String>
    ) : AuthEvent

    data class AuthUserDeleted(
        override val emailAddress: String
    ) : AuthEvent
}