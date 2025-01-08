package jp.inaba.service.infrastructure.projector.product

import jp.inaba.service.infrastructure.jpa.basket.BasketJpaRepository
import jp.inaba.service.infrastructure.jpa.product.ProductJpaEntity
import jp.inaba.service.infrastructure.jpa.product.ProductJpaRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component
import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.message.product.event.ProductUpdatedEvent
import jp.inaba.message.product.event.ProductDeletedEvent

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
