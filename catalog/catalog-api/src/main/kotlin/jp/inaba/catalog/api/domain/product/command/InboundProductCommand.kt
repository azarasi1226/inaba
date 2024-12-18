package jp.inaba.catalog.api.domain.product.command

import jp.inaba.catalog.share.domain.product.ProductId
import jp.inaba.catalog.share.domain.product.ProductQuantity

data class InboundProductCommand(
    override val id: ProductId,
    val quantity: ProductQuantity,
) : ProductAggregateCommand