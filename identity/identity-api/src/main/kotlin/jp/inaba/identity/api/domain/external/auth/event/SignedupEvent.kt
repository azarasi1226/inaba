package jp.inaba.identity.api.domain.external.auth.event

data class SignedupEvent(
    override val emailAddress: String,
    val password: String,
) : AuthEvent