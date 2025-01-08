package jp.inaba.message.basket.command

import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.basket.BasketItemQuantity
import jp.inaba.core.domain.product.ProductId
import org.axonframework.commandhandling.RoutingKey

data class SetBasketItemCommand(
    @get:RoutingKey
    val id: BasketId,
    val productId: ProductId,
    val basketItemQuantity: BasketItemQuantity,
)
