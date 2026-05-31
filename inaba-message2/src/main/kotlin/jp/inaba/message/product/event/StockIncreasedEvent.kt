package jp.inaba.message.product.event

import jp.inaba.message.InabaEventTag
import org.axonframework.eventsourcing.annotation.EventTag

data class StockIncreasedEvent(
    @EventTag(key = InabaEventTag.PRODUCT_ID)
    val id: String,
    val idempotencyId: String,
    val increaseStockQuantity: Int,
    val increasedStockQuantity: Int,
)
