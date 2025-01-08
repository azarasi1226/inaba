package jp.inaba.message.product.command

import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.product.ProductQuantity

data class ShipmentProductCommand(
    override val id: ProductId,
    val quantity: ProductQuantity,
) : ProductAggregateCommand
