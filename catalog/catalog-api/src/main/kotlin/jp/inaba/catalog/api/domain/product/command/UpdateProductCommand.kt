package jp.inaba.catalog.api.domain.product.command

import jp.inaba.catalog.share.domain.product.ProductDescription
import jp.inaba.catalog.share.domain.product.ProductId
import jp.inaba.catalog.share.domain.product.ProductImageURL
import jp.inaba.catalog.share.domain.product.ProductName
import jp.inaba.catalog.share.domain.product.ProductPrice

data class UpdateProductCommand(
    override val id: ProductId,
    val name: ProductName,
    val description: ProductDescription,
    val imageUrl: ProductImageURL?,
    val price: ProductPrice,
) : ProductAggregateCommand
