package jp.inaba.message.product.command

import jp.inaba.core.domain.product.ProductDescription
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.product.ProductImageURL
import jp.inaba.core.domain.product.ProductName
import jp.inaba.core.domain.product.ProductPrice
import jp.inaba.core.domain.product.ProductQuantity

data class CreateProductCommand(
    override val id: ProductId,
    val name: ProductName,
    val description: ProductDescription,
    val imageUrl: ProductImageURL?,
    val price: ProductPrice,
    val quantity: ProductQuantity,
) : ProductAggregateCommand
