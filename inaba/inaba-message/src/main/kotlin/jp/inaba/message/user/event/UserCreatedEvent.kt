package jp.inaba.message.user.event

data class UserCreatedEvent(
    val id: String,
    val subject: String,
)
