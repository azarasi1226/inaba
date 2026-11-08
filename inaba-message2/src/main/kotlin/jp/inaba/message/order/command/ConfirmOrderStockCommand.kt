package jp.inaba.message.order.command

import jp.inaba.core.domain.order.OrderId
import jp.inaba.core.domain.product.ProductId
import jp.inaba.message.CommandResult
import jp.inaba.message.Error
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.annotation.TargetEntityId

/**
 * オーダーが確保していた在庫を確定(物理在庫を減算)するコマンド。
 *
 * 確定する数量は確保イベント(order ストリーム)から復元するので持たないが、
 * 確定後の在庫を計算するために各 product ストリームを読む必要がある。
 * よって [TargetId] は orderId + productIds の多ストリーム境界(確保コマンドと対称)。
 * productIds は Saga が保持している注文明細から渡す。
 */
data class ConfirmOrderStockCommand(
    val orderId: OrderId,
    val productIds: List<ProductId>,
) {
    data class TargetId(
        val orderId: OrderId,
        val productIds: List<ProductId>,
    )

    @get:TargetEntityId
    private val targetId: TargetId
        get() = TargetId(orderId = orderId, productIds = productIds)
}

object ConfirmOrderStockResult {
    fun success() = CommandResult.success()

    fun noReservation() =
        CommandResult.fail(Error("確定対象の在庫確保がありません"))

    fun insufficientStock(productIds: List<String>) =
        CommandResult.fail(Error("確定時に在庫が不足しています: $productIds"))
}

fun CommandGateway.confirmOrderStock(command: ConfirmOrderStockCommand): CommandResult =
    this.sendAndWait(command, CommandResult::class.java)
