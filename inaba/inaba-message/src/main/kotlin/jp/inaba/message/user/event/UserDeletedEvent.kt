package jp.inaba.message.user.event

data class UserDeletedEvent(
    override val id: String,
) : UserEvent
