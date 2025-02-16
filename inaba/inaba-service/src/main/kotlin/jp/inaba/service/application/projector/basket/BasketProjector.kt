package jp.inaba.service.application.projector.basket

import jp.inaba.message.basket.event.BasketClearedEvent
import jp.inaba.message.basket.event.BasketItemDeletedEvent
import jp.inaba.message.basket.event.BasketItemSetEvent
import jp.inaba.message.product.event.ProductDeletedEvent
import jp.inaba.service.infrastructure.jpa.basket.BasketItemId
import jp.inaba.service.infrastructure.jpa.basket.BasketJpaEntity
import jp.inaba.service.infrastructure.jpa.basket.BasketJpaRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(BasketProjectorEventProcessor.PROCESSOR_NAME)
class BasketProjector(
    private val basketJpaRepository: BasketJpaRepository
) {
    @EventHandler
    fun on(event: BasketItemSetEvent) {
        val id =
            BasketItemId(
                basketId = event.id,
                productId = event.productId,
            )

        val basketItemJpaEntity =
            BasketJpaEntity(
                basketItemId = id,
                itemQuantity = event.basketItemQuantity,
            )

        basketJpaRepository.save(basketItemJpaEntity)
    }

    @EventHandler
    fun on(event: BasketItemDeletedEvent) {
        basketJpaRepository.deleteByBasketIdAndProductId(
            basketId = event.id,
            productId = event.productId,
        )
    }

    @EventHandler
    fun on(event: BasketClearedEvent) {
        basketJpaRepository.deleteByBasketItemId_BasketId(event.id)
    }

    @EventHandler
    fun on(event: ProductDeletedEvent) {
        basketJpaRepository.deleteByBasketItemId_ProductId(event.id)
    }
}
