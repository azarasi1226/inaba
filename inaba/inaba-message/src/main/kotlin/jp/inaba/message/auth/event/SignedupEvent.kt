package jp.inaba.message.auth.event

data class SignedupEvent(
    override val emailAddress: String,
    val password: String,
) : AuthEvent
