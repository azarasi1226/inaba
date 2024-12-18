package jp.inaba.catalog.api.domain.product.command

import jp.inaba.catalog.share.domain.product.ProductDescription
import jp.inaba.catalog.share.domain.product.ProductId
import jp.inaba.catalog.share.domain.product.ProductImageURL
import jp.inaba.catalog.share.domain.product.ProductName
import jp.inaba.catalog.share.domain.product.ProductPrice
import jp.inaba.catalog.share.domain.product.ProductQuantity

data class CreateProductCommand(
    override val id: ProductId,
    val name: ProductName,
    val description: ProductDescription,
    val imageUrl: ProductImageURL?,
    val price: ProductPrice,
    val quantity: ProductQuantity,
) : ProductAggregateCommand
