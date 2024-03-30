package jp.inaba.basket.service.application.query.projector.product

import jp.inaba.basket.service.application.query.projector.basketitem.BasketItemProjectorEventProcessor
import jp.inaba.basket.service.infrastructure.jpa.product.ProductJpaEntity
import jp.inaba.basket.service.infrastructure.jpa.product.ProductJpaRepository
import jp.inaba.catalog.api.domain.product.ProductEvents
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(ProductProjectorEventProcessor.PROCESSOR_NAME)
class ProductProjector(
    private val repository: ProductJpaRepository
){
    @EventHandler
    fun on(event: ProductEvents.Created) {
        val entity = ProductJpaEntity(
            id = event.id,
            name = event.name,
            imageUrl = event.imageUrl,
            price = event.price
        )

        repository.save(entity)
    }

    @EventHandler
    fun on(event: ProductEvents.Updated) {
        val entity = repository.findById(event.id)
            .orElseThrow()

        entity.name = event.name
        entity.imageUrl = event.imageUrl
        entity.price = event.price

        repository.save(entity)
    }

    @EventHandler
    fun on(event: ProductEvents.Deleted) {
        repository.deleteById(event.id)
    }
}