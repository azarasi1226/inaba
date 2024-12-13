package jp.inaba.identity.api.domain.external.auth.event

data class ConfirmCodeResentEvent(
    override val emailAddress: String,
) : AuthEvent