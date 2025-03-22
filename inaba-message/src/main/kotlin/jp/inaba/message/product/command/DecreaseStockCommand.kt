package jp.inaba.message.product.command

import jp.inaba.core.domain.common.IdempotencyId
import jp.inaba.core.domain.product.DecreaseStockQuantity
import jp.inaba.core.domain.product.ProductId

data class DecreaseStockCommand(
    override val id: ProductId,
    val idempotencyId: IdempotencyId,
    val decreaseStockQuantity: DecreaseStockQuantity,
) : ProductAggregateCommand
