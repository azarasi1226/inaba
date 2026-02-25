package jp.inaba.message.product.command

import jp.inaba.core.domain.common.IdempotencyId
import jp.inaba.core.domain.product.IncreaseStockQuantity
import jp.inaba.core.domain.product.ProductId
import org.axonframework.modelling.annotation.TargetEntityId

data class IncreaseStockCommand(
    @TargetEntityId
    val id: ProductId,
    val idempotencyId: IdempotencyId,
    val increaseStockQuantity: IncreaseStockQuantity,
)
