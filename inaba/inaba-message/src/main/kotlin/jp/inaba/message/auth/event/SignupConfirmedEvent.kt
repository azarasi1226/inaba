package jp.inaba.message.auth.event

data class SignupConfirmedEvent(
    override val emailAddress: String,
    val confirmCode: String,
) : AuthEvent
