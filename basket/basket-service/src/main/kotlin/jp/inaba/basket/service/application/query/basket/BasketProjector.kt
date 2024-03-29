package jp.inaba.basket.service.application.query.basket

import jp.inaba.basket.api.domain.basket.BasketEvents
import jp.inaba.basket.service.infrastructure.jpa.basketitem.BasketItemId
import jp.inaba.basket.service.infrastructure.jpa.basketitem.BasketItemJpaEntity
import jp.inaba.basket.service.infrastructure.jpa.basketitem.BasketItemJpaRepository
import jp.inaba.basket.service.infrastructure.jpa.product.ProductJpaEntity
import jp.inaba.basket.service.infrastructure.jpa.product.ProductJpaRepository
import jp.inaba.catalog.api.domain.product.ProductEvents
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(BasketProjectorEventProcessor.PROCESSOR_NAME)
class BasketProjector(
    private val productJpaRepository: ProductJpaRepository,
    private val basketItemJpaRepository: BasketItemJpaRepository
) {
    @ResetHandler
    fun reset() {
        productJpaRepository.deleteAll()
        basketItemJpaRepository.deleteAll()
    }

    // -------------------------------
    // ------------product------------
    // -------------------------------
    @EventHandler
    fun on(event: ProductEvents.Created) {
        val entity = ProductJpaEntity(
            id = event.id,
            name = event.name,
            imageUrl = event.imageUrl,
            price = event.price
        )

        productJpaRepository.save(entity)
    }

    @EventHandler
    fun on(event: ProductEvents.Updated) {
        val entity = productJpaRepository.findById(event.id)
            .orElseThrow()

        entity.name = event.name
        entity.imageUrl = event.imageUrl
        entity.price = event.price

        productJpaRepository.save(entity)
    }

    @EventHandler
    fun on(event: ProductEvents.Deleted) {
        basketItemJpaRepository.deleteByProductId(event.id)
        productJpaRepository.deleteById(event.id)
    }


    // -------------------------------
    // ----------basket_item----------
    // -------------------------------
    @EventHandler
    fun on(event: BasketEvents.BasketItemSet) {
        val productJpaEntity = productJpaRepository.findById(event.productId)
            .orElseThrow { Exception("Productが存在しません") }

        val id = BasketItemId(
            basketId = event.id,
            productId = event.productId
        )

        val basketItemJpaEntity = BasketItemJpaEntity(
            basketItemId = id,
            basketId = event.id,
            product = productJpaEntity,
            itemQuantity = event.basketItemQuantity
        )

        basketItemJpaRepository.save(basketItemJpaEntity)
    }

    @EventHandler
    fun on(event: BasketEvents.BasketItemDeleted) {
        basketItemJpaRepository.deleteByBasketIdAndProductId(
            basketId = event.id,
            productId = event.productId
        )
    }

    @EventHandler
    fun on(event: BasketEvents.Cleared) {
        basketItemJpaRepository.deleteByBasketId(event.id)
    }
}