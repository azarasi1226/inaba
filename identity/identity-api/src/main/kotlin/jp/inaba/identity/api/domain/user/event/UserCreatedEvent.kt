package jp.inaba.identity.api.domain.user.event

data class UserCreatedEvent(
    override val id: String,
) : UserEvent