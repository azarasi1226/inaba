package jp.inaba.identity.service.presentation.auth.confirmsignup

data class ConfirmSignupRequest(
    val emailAddress: String,
    val confirmCode: String
)