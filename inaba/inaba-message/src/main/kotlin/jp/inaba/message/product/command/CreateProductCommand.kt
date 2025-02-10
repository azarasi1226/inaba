package jp.inaba.message.product.command

import jp.inaba.core.domain.product.ProductDescription
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.product.ProductImageURL
import jp.inaba.core.domain.product.ProductName
import jp.inaba.core.domain.product.ProductPrice
import org.axonframework.commandhandling.RoutingKey

data class CreateProductCommand(
    @RoutingKey
    val id: ProductId,
    val name: ProductName,
    val description: ProductDescription,
    val imageUrl: ProductImageURL?,
    val price: ProductPrice,
)
