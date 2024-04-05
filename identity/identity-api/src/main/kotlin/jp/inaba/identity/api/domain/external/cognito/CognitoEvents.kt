package jp.inaba.identity.api.domain.external.cognito



interface CognitoEvent {
    val sub: String
}

object CognitoEvents {
    data class Signedup(
        override val sub: String,
        val emailAddress: String,
        val password: String
    ) : CognitoEvent

    data class SignupConfirmed(
        override val sub: String,
        val emailAddress: String,
        val confirmCode: String
    ) : CognitoEvent

    data class ConfirmCodeResent(
        override val sub: String,
        val emailAddress: String
    ) : CognitoEvent

    data class CustomAttributeForUserIdUpdated(
        override val sub: String,
        val emailAddress: String,
        val userId: String
    ) : CognitoEvent
}