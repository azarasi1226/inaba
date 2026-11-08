package jp.inaba.message.order.event

import jp.inaba.message.InabaEventTag
import org.axonframework.eventsourcing.annotation.EventTag

/**
 * オーダーのために在庫を「確保(予約)」したことを表すイベント。
 *
 * 確定減算([StockDecreasedEvent])とは別物。確保はまだ取り消せる前段で、
 * 配送まで確定したら確定減算へ進み、失敗したら [StockReservationReleasedEvent] で戻す。
 *
 * product_id と order_id の 2 タグを付けることで、
 *  - product 側: 在庫確保の整合性境界(二重売り防止の楽観ロック対象)
 *  - order 側  : 注文単位での冪等判定・補償(解放)
 * の両方から引けるようにしている。
 */
data class StockReservedEvent(
    @EventTag(key = InabaEventTag.PRODUCT_ID)
    val productId: String,
    @EventTag(key = InabaEventTag.ORDER_ID)
    val orderId: String,
    val quantity: Int,
)
