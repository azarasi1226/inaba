package jp.inaba.basket.api.domain.basket.command

import jp.inaba.basket.share.domain.basket.BasketId
import jp.inaba.basket.share.domain.basket.BasketItemQuantity
import jp.inaba.catalog.api.domain.product.ProductId
import org.axonframework.commandhandling.RoutingKey

data class SetBasketItemCommand(
    @get:RoutingKey
    val id: BasketId,
    val productId: ProductId,
    val basketItemQuantity: BasketItemQuantity,
)