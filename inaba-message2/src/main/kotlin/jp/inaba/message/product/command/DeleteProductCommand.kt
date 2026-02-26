package jp.inaba.message.product.command

import jp.inaba.core.domain.product.ProductId
import jp.inaba.message.CommandResult
import jp.inaba.message.Error
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.annotation.TargetEntityId

data class DeleteProductCommand(
    @TargetEntityId
    val id: ProductId,
)

object DeleteProductResult {
    fun success() = CommandResult.success()
    fun notFound() = CommandResult.faile(Error("商品が存在しませんでした"))
}

fun CommandGateway.deleteProduct(command: DeleteProductCommand): CommandResult =
    this.sendAndWait(command, CommandResult::class.java)
