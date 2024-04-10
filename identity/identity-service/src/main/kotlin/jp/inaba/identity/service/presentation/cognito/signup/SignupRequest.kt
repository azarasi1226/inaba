package jp.inaba.identity.service.presentation.cognito.signup

data class SignupRequest(
    val emailAddress: String,
    val password: String
)