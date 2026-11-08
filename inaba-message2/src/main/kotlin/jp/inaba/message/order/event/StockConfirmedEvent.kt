package jp.inaba.message.order.event

import jp.inaba.message.InabaEventTag
import org.axonframework.eventsourcing.annotation.EventTag

/**
 * 確保していた在庫を「確定」したことを表すイベント。
 * OrderSaga 図の最終段(配送成功 → オーダー終了)で発火し、ここで初めて
 * 物理在庫([decreasedStockQuantity])が確定的に減る。
 *
 * 案B: 確定を最後まで遅延させることで、途中で失敗しても補償は
 * [StockReservationReleasedEvent](解放)1 種類だけで済む。
 *
 * product_id と order_id の 2 タグを持つ:
 *  - product 側: 在庫数([decreasedStockQuantity])の確定更新。onHand を読む全 State がこれを畳み込む必要がある。
 *  - order 側  : 注文単位での冪等判定・確保の消し込み。
 */
data class StockConfirmedEvent(
    @EventTag(key = InabaEventTag.PRODUCT_ID)
    val productId: String,
    @EventTag(key = InabaEventTag.ORDER_ID)
    val orderId: String,
    // 今回確定(=確保から確定へ移した)数量
    val confirmedQuantity: Int,
    // 確定後の物理在庫(StockDecreasedEvent.decreasedStockQuantity と同じ絶対値セマンティクス)
    val decreasedStockQuantity: Int,
)
