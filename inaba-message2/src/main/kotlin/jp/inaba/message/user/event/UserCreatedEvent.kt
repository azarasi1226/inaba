package jp.inaba.message.user.event

import jp.inaba.message.InabaEventTag
import org.axonframework.eventsourcing.annotation.EventTag

data class UserCreatedEvent(
    @EventTag(key = InabaEventTag.USER_ID)
    val id: String,
    val subject: String,
)
