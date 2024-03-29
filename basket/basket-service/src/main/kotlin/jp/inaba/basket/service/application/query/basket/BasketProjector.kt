package jp.inaba.basket.service.application.query.basket

import jp.inaba.basket.api.domain.basket.BasketEvents
import jp.inaba.basket.service.infrastructure.jpa.basketitem.BasketItemId
import jp.inaba.basket.service.infrastructure.jpa.basketitem.BasketItemJpaEntity
import jp.inaba.basket.service.infrastructure.jpa.basketitem.BasketItemJpaRepository
import jp.inaba.basket.service.infrastructure.jpa.product.ProductJpaRepository
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
class BasketProjector(
    private val productJpaRepository: ProductJpaRepository,
    private val basketItemJpaRepository: BasketItemJpaRepository
) {
    @EventHandler
    fun on(event: BasketEvents.BasketItemSet) {
        val productJpaEntity = productJpaRepository.findById(event.productId)
            .orElseThrow { Exception("Productが存在しません") }

        val id = BasketItemId(
            basketId = event.id,
            productId = event.productId
        )

        val basketItemJpaEntity = BasketItemJpaEntity(
            basketItemId = id,
            basketId = event.id,
            product = productJpaEntity,
            itemQuantity = event.basketItemQuantity
        )

        basketItemJpaRepository.save(basketItemJpaEntity)
    }

    @EventHandler
    fun on(event: BasketEvents.BasketItemDeleted) {
        basketItemJpaRepository.deleteByBasketIdAndProductId(
            basketId = event.id,
            productId = event.productId
        )
    }

    @EventHandler
    fun on(event: BasketEvents.Cleared) {
        basketItemJpaRepository.deleteByBasketId(event.id)
    }
}