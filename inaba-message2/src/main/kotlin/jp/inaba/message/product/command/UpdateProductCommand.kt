package jp.inaba.message.product.command

import jp.inaba.core.domain.product.ProductDescription
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.product.ProductImageURL
import jp.inaba.core.domain.product.ProductName
import jp.inaba.core.domain.product.ProductPrice
import jp.inaba.message.CommandResult
import jp.inaba.message.Error
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.annotation.TargetEntityId

data class UpdateProductCommand(
    @TargetEntityId
    val id: ProductId,
    val name: ProductName,
    val description: ProductDescription,
    val imageUrl: ProductImageURL?,
    val price: ProductPrice,
)

object UpdateProductResult {
    fun success() = CommandResult.success()
    fun notFound() = CommandResult.faile(Error("商品が存在しませんでした"))
    fun deleted() = CommandResult.faile(Error("商品は削除済みです"))
}

fun CommandGateway.updateProduct(command: UpdateProductCommand): CommandResult =
    this.sendAndWait(command, CommandResult::class.java)
