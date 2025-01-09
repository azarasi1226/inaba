package jp.inaba.message.auth.event

data class AuthUserDeletedEvent(
    override val emailAddress: String,
) : AuthEvent
