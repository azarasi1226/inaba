package jp.inaba.message.basket.command

import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.user.UserId
import org.axonframework.modelling.annotation.TargetEntityId

data class DeleteBasketItemCommand(
    @TargetEntityId
    val id: UserId,
    val productId: ProductId,
)
