package jp.inaba.message.product.event

import jp.inaba.message.InabaEventTag
import org.axonframework.eventsourcing.annotation.EventTag

data class ProductCreatedEvent(
    @EventTag(key = InabaEventTag.PRODUCT_ID)
    val id: String,
    @EventTag(key = InabaEventTag.BRAND_ID)
    val brandId: String,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Int,
    val quantity: Int,
)
