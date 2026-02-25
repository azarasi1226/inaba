package jp.inaba.message.basket.event

import jp.inaba.message.InabaEventTag
import org.axonframework.eventsourcing.annotation.EventTag

data class BasketItemSetEvent(
    @EventTag(key = InabaEventTag.USER_ID)
    val userId: String,
    @EventTag(key = InabaEventTag.PRODUCT_ID)
    val productId: String,
    val basketItemQuantity: Int,
)
