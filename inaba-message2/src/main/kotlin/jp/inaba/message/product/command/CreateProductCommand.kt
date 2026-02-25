package jp.inaba.message.product.command

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.product.ProductDescription
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.product.ProductImageURL
import jp.inaba.core.domain.product.ProductName
import jp.inaba.core.domain.product.ProductPrice
import jp.inaba.core.domain.product.StockQuantity
import jp.inaba.message.Error
import jp.inaba.message.UseCaseResult
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

class CreateProductResult private constructor(
    override val success: Boolean, override val error: Error?,
) : UseCaseResult() {
    companion object {
        fun success(): CreateProductResult = CreateProductResult(true, null)
        fun alreadyExists(): CreateProductResult = CreateProductResult(false, Error("商品が既に存在しています"))
        fun brandNotFound(): CreateProductResult = CreateProductResult(false, Error("ブランドが存在しません"))
    }
}

