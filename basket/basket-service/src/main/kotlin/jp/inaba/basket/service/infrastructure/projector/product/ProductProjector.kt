package jp.inaba.basket.service.infrastructure.projector.product

import jp.inaba.basket.service.infrastructure.jpa.basket.BasketJpaRepository
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
    private val productRepository: ProductJpaRepository,
    private val basketRepository: BasketJpaRepository,
) {
    @EventHandler
    fun on(event: ProductCreatedEvent) {
        val product =
            ProductJpaEntity(
                id = event.id,
                name = event.name,
                imageUrl = event.imageUrl,
                price = event.price,
            )

        productRepository.save(product)
    }

    @EventHandler
    fun on(event: ProductUpdatedEvent) {
        productRepository.findById(event.id)
            .ifPresent {
                val updatedProduct =
                    it.copy(
                        name = event.name,
                        imageUrl = event.imageUrl,
                        price = event.price,
                    )

                productRepository.save(updatedProduct)
            }
    }

    @EventHandler
    fun on(event: ProductDeletedEvent) {
        basketRepository.deleteByProductId(event.id)
        productRepository.deleteById(event.id)
    }
}
