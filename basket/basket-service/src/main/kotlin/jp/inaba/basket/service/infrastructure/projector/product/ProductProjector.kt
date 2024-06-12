package jp.inaba.basket.service.infrastructure.projector.product

import jp.inaba.basket.service.infrastructure.jpa.product.ProductJpaEntity
import jp.inaba.basket.service.infrastructure.jpa.product.ProductJpaRepository
import jp.inaba.catalog.api.domain.product.ProductCreatedEvent
import jp.inaba.catalog.api.domain.product.ProductDeletedEvent
import jp.inaba.catalog.api.domain.product.ProductUpdatedEvent
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(ProductProjectorEventProcessor.PROCESSOR_NAME)
class ProductProjector(
    private val repository: ProductJpaRepository,
) {
    @EventHandler
    fun on(event: ProductCreatedEvent) {
        val entity =
            ProductJpaEntity(
                id = event.id,
                name = event.name,
                imageUrl = event.imageUrl,
                price = event.price,
            )

        repository.save(entity)
    }

    @EventHandler
    fun on(event: ProductUpdatedEvent) {
        val entity =
            repository.findById(event.id)
                .orElseThrow()

        entity.name = event.name
        entity.imageUrl = event.imageUrl
        entity.price = event.price

        repository.save(entity)
    }

    @EventHandler
    fun on(event: ProductDeletedEvent) {
        // TODO("このままだと確定でエラーになる。先にbasketItem消すか、ステータス方式にしなければ")
        repository.deleteById(event.id)
    }
}
