package jp.inaba.service.domain.product

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.product.ProductDescription
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.product.ProductImageURL
import jp.inaba.core.domain.product.ProductName
import jp.inaba.core.domain.product.ProductPrice
import jp.inaba.message.product.command.ProductAggregateCommand

data class InternalCreateProductCommand(
    override val id: ProductId,
    val brandId: BrandId,
    val name: ProductName,
    val description: ProductDescription,
    val imageUrl: ProductImageURL?,
    val price: ProductPrice,
) : ProductAggregateCommand
