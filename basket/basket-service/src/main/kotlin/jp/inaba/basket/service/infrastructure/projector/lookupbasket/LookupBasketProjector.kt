package jp.inaba.basket.service.infrastructure.projector.lookupbasket

import jp.inaba.basket.api.domain.basket.BasketCreatedEvent
import jp.inaba.basket.service.infrastructure.jpa.lookupbasket.LookupBasketJpaEntity
import jp.inaba.basket.service.infrastructure.jpa.lookupbasket.LookupBasketJpaRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(LookupBasketProjectorEventProcessor.PROCESSOR_NAME)
class LookupBasketProjector(
    private val lookupBasketRepository: LookupBasketJpaRepository,
) {
    @EventHandler
    fun on(event: BasketCreatedEvent) {
        val lookupBasket =
            LookupBasketJpaEntity(
                basketId = event.id,
                userId = event.userId,
            )

        lookupBasketRepository.save(lookupBasket)
    }
}
