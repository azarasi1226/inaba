package jp.inaba.identity.api.domain.external.auth.event

data class IdTokenAttributeForUserIdUpdatedEvent(
    override val emailAddress: String,
    val userId: String,
) : AuthEvent