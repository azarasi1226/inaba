package jp.inaba.basket.service.application.query.basket

import de.huxhorn.sulky.ulid.ULID
import jp.inaba.basket.api.domain.basket.BasketEvents
import jp.inaba.basket.service.infrastructure.jpa.basket.BasketJpaEntity
import jp.inaba.basket.service.infrastructure.jpa.basket.BasketJpaRepository
import jp.inaba.basket.service.infrastructure.jpa.basketitem.BasketItemJpaEntity
import jp.inaba.basket.service.infrastructure.jpa.basketitem.BasketItemJpaRepository
import jp.inaba.basket.service.infrastructure.jpa.product.ProductJpaRepository
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
class BasketProjector(
    private val basketJpaRepository: BasketJpaRepository,
    private val itemJpaRepository: ProductJpaRepository,
    private val basketItemJpaRepository: BasketItemJpaRepository
) {
    @EventHandler
    fun on(event: BasketEvents.Created) {
        val basket = BasketJpaEntity(
            basketId = event.id,
            userId = event.userId
        )

        basketJpaRepository.save(basket)
    }

    @EventHandler
    fun on(event: BasketEvents.BasketItemSet) {
        val basketJpaEntity = basketJpaRepository.findById(event.id)
            .orElseThrow { Exception("Basketが存在しません") }

        val productJpaEntity = itemJpaRepository.findById(event.id)
            .orElseThrow { Exception("aaaa") }

        val basketItemJpaEntity = BasketItemJpaEntity(
            basketItemId = ULID().nextULID(),
            basket = basketJpaEntity,
            product = productJpaEntity,
            itemQuantity = event.basketItemQuantity
        )

        basketItemJpaRepository.save(basketItemJpaEntity)
    }

    @EventHandler
    fun on(event: BasketEvents.BasketItemDeleted) {
        basketItemJpaRepository.deleteByBasketIdAndItemId(
            basketId = event.id,
            itemId = event.productId
        )
    }

    @EventHandler
    fun on(event: BasketEvents.Cleared) {
        basketItemJpaRepository.deleteByBasketId(event.id)
    }
}