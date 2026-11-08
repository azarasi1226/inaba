package jp.inaba.message.order.command

import jp.inaba.core.domain.order.OrderId
import jp.inaba.core.domain.product.DecreaseStockQuantity
import jp.inaba.core.domain.product.ProductId
import jp.inaba.message.CommandResult
import jp.inaba.message.Error
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.annotation.TargetEntityId

/**
 * オーダー内の全商品の在庫を all-or-nothing で確保するコマンド。
 *
 * これが DCB(Dynamic Consistency Boundary)の入口。
 * [TargetId.productIds] が「この注文が触る商品の集合」= 整合性境界 で、注文ごとに動的に変わる。
 * 既存の [jp.inaba.message.basket.command.SetBasketItemCommand.TargetId](user + product の 2 点)を、
 * order + 商品 N 件に拡張したもの。
 */
data class ReserveOrderStockCommand(
    val orderId: OrderId,
    val lines: List<Line>,
) {
    data class Line(
        val productId: ProductId,
        val quantity: DecreaseStockQuantity,
    )

    data class TargetId(
        val orderId: OrderId,
        val productIds: List<ProductId>,
    )

    @get:TargetEntityId
    private val targetId: TargetId
        get() = TargetId(orderId = orderId, productIds = lines.map { it.productId })
}

object ReserveOrderStockResult {
    fun success() = CommandResult.success()

    fun productNotFound(productIds: List<String>) =
        CommandResult.fail(Error("商品が存在しません: $productIds"))

    fun outOfStock(productIds: List<String>) =
        CommandResult.fail(Error("在庫が不足しています: $productIds"))
}

fun CommandGateway.reserveOrderStock(command: ReserveOrderStockCommand): CommandResult =
    this.sendAndWait(command, CommandResult::class.java)
