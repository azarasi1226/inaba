package jp.inaba.message.product.command

import jp.inaba.core.domain.common.IdempotencyId
import jp.inaba.core.domain.product.IncreaseStockQuantity
import jp.inaba.core.domain.product.ProductId
import jp.inaba.message.CommandResult
import jp.inaba.message.Error
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.annotation.TargetEntityId

data class IncreaseStockCommand(
    @TargetEntityId
    val id: ProductId,
    val idempotencyId: IdempotencyId,
    val increaseStockQuantity: IncreaseStockQuantity,
)

object IncreaseStockResult {
    fun success() = CommandResult.success()
    fun notFound() = CommandResult.faile(Error("商品が存在しませんでした"))
    fun deleted() = CommandResult.faile(Error("商品は削除済みです"))
    fun cannotReceive() = CommandResult.faile(Error("在庫が上限に達しています"))
}

fun CommandGateway.increaseStock(command: IncreaseStockCommand): CommandResult =
    this.sendAndWait(command, CommandResult::class.java)
