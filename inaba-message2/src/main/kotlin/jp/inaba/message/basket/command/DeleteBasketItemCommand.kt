package jp.inaba.message.basket.command

import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.user.UserId
import jp.inaba.message.CommandResult
import jp.inaba.message.Error
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.annotation.TargetEntityId

data class DeleteBasketItemCommand(
    @TargetEntityId
    val id: UserId,
    val productId: ProductId,
)

object DeleteBasketItemResult {
    fun success() = CommandResult.success()
    fun userNotFound() = CommandResult.faile(Error("ユーザーが存在しません"))
}

fun CommandGateway.deleteBasketItem(command: DeleteBasketItemCommand): CommandResult =
    this.sendAndWait(command, CommandResult::class.java)
