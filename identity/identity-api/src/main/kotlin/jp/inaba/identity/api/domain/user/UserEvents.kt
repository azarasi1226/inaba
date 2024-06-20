package jp.inaba.identity.api.domain.user

interface UserEvent {
    val id: String
}

data class UserCreatedEvent(
    override val id: String,
) : UserEvent

data class UserDeletedEvent(
    override val id: String,
) : UserEvent
