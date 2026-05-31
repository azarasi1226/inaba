package jp.inaba.message.product.command

import jp.inaba.core.domain.common.IdempotencyId
import jp.inaba.core.domain.product.DecreaseStockQuantity
import jp.inaba.core.domain.product.ProductId
import jp.inaba.message.CommandResult
import jp.inaba.message.Error
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.annotation.TargetEntityId

data class DecreaseStockCommand(
    @TargetEntityId
    val id: ProductId,
    val idempotencyId: IdempotencyId,
    val decreaseStockQuantity: DecreaseStockQuantity,
)

object DecreaseStockResult {
    fun success() = CommandResult.success()
    fun notFound() = CommandResult.faile(Error("商品が存在しませんでした"))
    fun deleted() = CommandResult.faile(Error("商品は削除済みです"))
    fun insufficientStock() = CommandResult.faile(Error("在庫が不足しています"))
}

fun CommandGateway.decreaseStock(command: DecreaseStockCommand): CommandResult =
    this.sendAndWait(command, CommandResult::class.java)
