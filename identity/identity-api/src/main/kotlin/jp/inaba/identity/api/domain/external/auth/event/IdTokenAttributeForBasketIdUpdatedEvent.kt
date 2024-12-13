package jp.inaba.identity.api.domain.external.auth.event

data class IdTokenAttributeForBasketIdUpdatedEvent(
    override val emailAddress: String,
    val basketId: String,
) : AuthEvent
