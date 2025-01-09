package jp.inaba.message.auth.event

data class IdTokenAttributeForBasketIdUpdatedEvent(
    override val emailAddress: String,
    val basketId: String,
) : AuthEvent
