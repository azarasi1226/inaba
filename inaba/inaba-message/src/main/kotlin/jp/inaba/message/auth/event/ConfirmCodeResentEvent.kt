package jp.inaba.message.auth.event

data class ConfirmCodeResentEvent(
    override val emailAddress: String,
) : AuthEvent
