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
import org.axonframework.eventhandling.ResetHandler
import org.axonframework.eventhandling.Timestamp
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Component
@ProcessingGroup(BasketProjectorEventProcessor.PROCESSOR_NAME)
class BasketProjector(
    private val repository: BasketJpaRepository,
) {
    @ResetHandler
    fun reset() {
        repository.deleteAllInBatch()
    }

    @EventHandler
    fun on(
        event: BasketItemSetEvent,
        @Timestamp timestamp: Instant,
    ) {
        val id =
            BasketItemId(
                basketId = event.id,
                productId = event.productId,
            )

        val basketItemJpaEntity =
            BasketJpaEntity(
                basketItemId = id,
                itemQuantity = event.basketItemQuantity,
                addedAt = LocalDateTime.ofInstant(timestamp, ZoneId.of("Asia/Tokyo")),
            )

        repository.save(basketItemJpaEntity)
    }

    @EventHandler
    fun on(event: BasketItemDeletedEvent) {
        repository.deleteByBasketIdAndProductId(
            basketId = event.id,
            productId = event.productId,
        )
    }

    @EventHandler
    fun on(event: BasketClearedEvent) {
        repository.deleteByBasketItemIdBasketId(event.id)
    }

    @EventHandler
    fun on(event: ProductDeletedEvent) {
        repository.deleteByBasketItemIdProductId(event.id)
    }
}
