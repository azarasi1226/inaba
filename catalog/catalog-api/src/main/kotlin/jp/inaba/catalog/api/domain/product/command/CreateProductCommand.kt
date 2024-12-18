package jp.inaba.catalog.api.domain.product.command

import jp.inaba.catalog.share.domain.product.*

data class CreateProductCommand(
    override val id: ProductId,
    val name: ProductName,
    val description: ProductDescription,
    val imageUrl: ProductImageURL?,
    val price: ProductPrice,
    val quantity: ProductQuantity,
) : ProductAggregateCommand