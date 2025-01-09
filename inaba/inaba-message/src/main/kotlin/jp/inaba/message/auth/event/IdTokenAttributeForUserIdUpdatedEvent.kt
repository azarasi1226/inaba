package jp.inaba.message.auth.event

data class IdTokenAttributeForUserIdUpdatedEvent(
    override val emailAddress: String,
    val userId: String,
) : AuthEvent
