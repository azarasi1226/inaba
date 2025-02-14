package jp.inaba.service.application.projector.basket

import jp.inaba.message.basket.event.BasketClearedEvent
import jp.inaba.message.basket.event.BasketItemDeletedEvent
import jp.inaba.message.basket.event.BasketItemSetEvent
import jp.inaba.service.infrastructure.jpa.basket.BasketItemId
import jp.inaba.service.infrastructure.jpa.basket.BasketJpaEntity
import jp.inaba.service.infrastructure.jpa.basket.BasketJpaRepository
import jp.inaba.service.infrastructure.jpa.product.ProductJpaRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(BasketProjectorEventProcessor.PROCESSOR_NAME)
class BasketProjector(
    private val productJpaRepository: ProductJpaRepository,
    private val basketJpaRepository: BasketJpaRepository,
) {
    @EventHandler
    fun on(event: BasketItemSetEvent) {
        // TODO(JPA詳しくねーけどここでProductとるのスキップできないかな。美しさがない。)
        val productJpaEntity = productJpaRepository.findById(event.productId).orElseThrow()

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
        basketJpaRepository.deleteByBasketId(event.id)
    }
}
