package jp.inaba.message.user.event

data class UserCreatedEvent(
    override val id: String,
) : UserEvent
