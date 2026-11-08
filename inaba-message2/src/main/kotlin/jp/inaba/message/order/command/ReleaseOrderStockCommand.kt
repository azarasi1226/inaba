package jp.inaba.message.order.command

import jp.inaba.core.domain.order.OrderId
import jp.inaba.message.CommandResult
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.annotation.TargetEntityId

/**
 * オーダーが確保していた在庫を全て解放する補償コマンド。
 * 確保イベントが order_id タグを持つので、注文ストリーム 1 本だけ読めば解放できる(境界が小さい)。
 */
data class ReleaseOrderStockCommand(
    val orderId: OrderId,
) {
    data class TargetId(val orderId: OrderId)

    @get:TargetEntityId
    private val targetId: TargetId
        get() = TargetId(orderId = orderId)
}

object ReleaseOrderStockResult {
    fun success() = CommandResult.success()
}

fun CommandGateway.releaseOrderStock(command: ReleaseOrderStockCommand): CommandResult =
    this.sendAndWait(command, CommandResult::class.java)
