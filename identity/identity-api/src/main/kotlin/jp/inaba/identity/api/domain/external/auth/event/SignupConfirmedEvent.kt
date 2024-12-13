package jp.inaba.identity.api.domain.external.auth.event

data class SignupConfirmedEvent(
    override val emailAddress: String,
    val confirmCode: String,
) : AuthEvent
