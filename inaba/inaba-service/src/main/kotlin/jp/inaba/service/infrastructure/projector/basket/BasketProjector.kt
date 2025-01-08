package jp.inaba.service.infrastructure.projector.basket

import jp.inaba.service.infrastructure.jpa.basket.BasketItemId
import jp.inaba.service.infrastructure.jpa.basket.BasketJpaEntity
import jp.inaba.service.infrastructure.jpa.basket.BasketJpaRepository
import jp.inaba.service.infrastructure.jpa.product.ProductJpaRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component
import jp.inaba.message.basket.event.*

@Component
@ProcessingGroup(BasketProjectorEventProcessor.PROCESSOR_NAME)
class BasketProjector(
    private val productRepository: ProductJpaRepository,
    private val basketRepository: BasketJpaRepository,
) {
    @EventHandler
    fun on(event: BasketItemSetEvent) {
        val productJpaEntity =
            productRepository.findById(event.productId)
                .orElseThrow { Exception("Productが存在しませんでした。event:[$event]") }

        val id =
            BasketItemId(
                basketId = event.id,
                productId = event.productId,
            )

        val basketItemJpaEntity =
            BasketJpaEntity(
                basketItemId = id,
                basketId = event.id,
                product = productJpaEntity,
                itemQuantity = event.basketItemQuantity,
            )

        basketRepository.save(basketItemJpaEntity)
    }

    @EventHandler
    fun on(event: BasketItemDeletedEvent) {
        basketRepository.deleteByBasketIdAndProductId(
            basketId = event.id,
            productId = event.productId,
        )
    }

    @EventHandler
    fun on(event: BasketClearedEvent) {
        basketRepository.deleteByBasketId(event.id)
    }
}
