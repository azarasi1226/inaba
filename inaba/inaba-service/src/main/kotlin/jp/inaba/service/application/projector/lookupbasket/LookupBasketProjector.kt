package jp.inaba.service.application.projector.lookupbasket

import jp.inaba.message.basket.event.BasketCreatedEvent
import jp.inaba.message.basket.event.BasketDeletedEvent
import jp.inaba.service.infrastructure.jpa.lookupbasket.LookupBasketJpaEntity
import jp.inaba.service.infrastructure.jpa.lookupbasket.LookupBasketJpaRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(LookupBasketProjectorEventProcessor.PROCESSOR_NAME)
class LookupBasketProjector(
    private val repository: LookupBasketJpaRepository,
) {
    @EventHandler
    fun on(event: BasketCreatedEvent) {
        val lookupBasket =
            LookupBasketJpaEntity(
                id = event.id,
                userId = event.userId,
            )

        repository.save(lookupBasket)
    }

    @EventHandler
    fun on(event: BasketDeletedEvent) {
        repository.deleteById(event.id)
    }
}
