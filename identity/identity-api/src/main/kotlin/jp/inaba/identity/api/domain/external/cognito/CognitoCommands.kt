package jp.inaba.identity.api.domain.external.cognito

class CognitoCommands {
}

data class SignupCognitoUserCommand(
    val emailAddress: String,
    val password: String
)