package jp.inaba.message.order.event

import jp.inaba.message.InabaEventTag
import org.axonframework.eventsourcing.annotation.EventTag

/**
 * 確保していた在庫を「解放」したことを表すイベント(補償)。
 * OrderSaga 図の「確保していた在庫を解放」に対応する。
 */
data class StockReservationReleasedEvent(
    @EventTag(key = InabaEventTag.PRODUCT_ID)
    val productId: String,
    @EventTag(key = InabaEventTag.ORDER_ID)
    val orderId: String,
    val quantity: Int,
)
