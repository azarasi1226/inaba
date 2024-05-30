package jp.inaba.identity.service.presentation.auth.signup

data class SignupRequest(
    val emailAddress: String,
    val password: String,
)
