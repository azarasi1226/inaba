package jp.inaba.message.product.command

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.product.ProductDescription
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.product.ProductImageURL
import jp.inaba.core.domain.product.ProductName
import jp.inaba.core.domain.product.ProductPrice
import jp.inaba.core.domain.product.StockQuantity
import jp.inaba.message.CommandResult
import jp.inaba.message.Error
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.annotation.TargetEntityId

data class CreateProductCommand(
    val id: ProductId,
    val brandId: BrandId,
    val name: ProductName,
    val description: ProductDescription,
    val imageUrl: ProductImageURL?,
    val price: ProductPrice,
    val quantity: StockQuantity,
) {
    data class TargetId(val productId: ProductId, val brandId: BrandId)
    @get:TargetEntityId
    private val targetId: TargetId
        get() = TargetId(productId = id, brandId = brandId)
}

object CreateProductResult {
    fun success() = CommandResult.success()
    fun alreadyExists() = CommandResult.faile(Error("商品が既に存在しています"))
    fun brandNotFound() = CommandResult.faile(Error("ブランドが存在しません"))
}

fun CommandGateway.createProduct(command: CreateProductCommand): CommandResult =
    this.sendAndWait(command, CommandResult::class.java)

