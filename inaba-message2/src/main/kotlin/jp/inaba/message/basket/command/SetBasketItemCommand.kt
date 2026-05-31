package jp.inaba.message.basket.command

import jp.inaba.core.domain.basket.BasketItemQuantity
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.user.UserId
import jp.inaba.message.CommandResult
import jp.inaba.message.Error
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.annotation.TargetEntityId

data class SetBasketItemCommand(
    val id: UserId,
    val productId: ProductId,
    val basketItemQuantity: BasketItemQuantity,
) {
    data class TargetId(val userId: UserId, val productId: ProductId)

    @get:TargetEntityId
    private val targetId: TargetId
        get() = TargetId(userId = id, productId = productId)
}

object SetBasketItemResult {
    fun success() = CommandResult.success()
    fun userNotFound() = CommandResult.faile(Error("ユーザーが存在しません"))
    fun productNotFound() = CommandResult.faile(Error("商品が存在しません"))
    fun productMaxKindOver() = CommandResult.faile(Error("商品種類の上限数に到達しました"))
}

fun CommandGateway.setBasketItem(command: SetBasketItemCommand): CommandResult =
    this.sendAndWait(command, CommandResult::class.java)
