package jp.inaba.message.basket.event

import jp.inaba.message.InabaEventTag
import org.axonframework.eventsourcing.annotation.EventTag

data class BasketClearedEvent(
    @EventTag(key = InabaEventTag.USER_ID)
    val userId: String,
)
