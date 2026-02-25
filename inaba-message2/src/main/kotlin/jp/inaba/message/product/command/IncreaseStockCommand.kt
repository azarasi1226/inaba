package jp.inaba.message.product.command

import jp.inaba.core.domain.common.IdempotencyId
import jp.inaba.core.domain.product.IncreaseStockQuantity
import jp.inaba.core.domain.product.ProductId

data class IncreaseStockCommand(
    override val id: ProductId,
    val idempotencyId: IdempotencyId,
    val increaseStockQuantity: IncreaseStockQuantity,
) : ProductAggregateCommand
