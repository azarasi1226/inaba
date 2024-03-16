package jp.inaba.catalog.service.application.query.product

import jp.inaba.catalog.api.domain.product.ProductEvents
import jp.inaba.catalog.service.infrastructure.jpa.product.ProductJpaEntity
import jp.inaba.catalog.service.infrastructure.jpa.product.ProductJpaRepository
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
class ProductProjector(
    private val productJpaRepository: ProductJpaRepository
) {
    @EventHandler
    fun on(event: ProductEvents.Created) {
        val entity = ProductJpaEntity(
            id = event.id.value,
            name = event.name,
            description = event.description,
            imageUrl = event.imageUrl,
            price = event.price,
            quantity = event.quantity
        )

        productJpaRepository.save(entity)
    }
}