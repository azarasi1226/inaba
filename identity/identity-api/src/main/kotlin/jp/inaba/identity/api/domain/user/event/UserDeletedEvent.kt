package jp.inaba.identity.api.domain.user.event

data class UserDeletedEvent(
    override val id: String,
) : UserEvent