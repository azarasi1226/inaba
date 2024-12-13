package jp.inaba.identity.api.domain.external.auth.event

data class AuthUserDeletedEvent(
    override val emailAddress: String,
) : AuthEvent
