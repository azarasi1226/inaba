package jp.inaba.basket.service.application.query.basket

import de.huxhorn.sulky.ulid.ULID
import jp.ianba.basket.api.domain.basket.BasketClearedEvent
import jp.ianba.basket.api.domain.basket.BasketCreatedEvent
import jp.ianba.basket.api.domain.basket.ItemDeletedEvent
import jp.ianba.basket.api.domain.basket.ItemSetEvent
import jp.inaba.basket.service.infrastructure.jpa.basket.BasketJpaEntity
import jp.inaba.basket.service.infrastructure.jpa.basket.BasketJpaRepository
import jp.inaba.basket.service.infrastructure.jpa.basketitem.BasketItemJpaEntity
import jp.inaba.basket.service.infrastructure.jpa.basketitem.BasketItemJpaRepository
import jp.inaba.basket.service.infrastructure.jpa.item.ItemJpaRepository
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
class BasketProjector(
    private val basketJpaRepository: BasketJpaRepository,
    private val itemJpaRepository: ItemJpaRepository,
    private val basketItemJpaRepository: BasketItemJpaRepository
) {
    @EventHandler
    fun on(event: BasketCreatedEvent) {
        val basket = BasketJpaEntity(
            basketId = event.id.value,
            userId = event.userId
        )

        basketJpaRepository.save(basket)
    }

    @EventHandler
    fun on(event: ItemSetEvent) {
        val basketJpaEntity = basketJpaRepository.findById(event.id.value)
            .orElseThrow { Exception("Basketが存在しません") }

        val itemJpaEntity = itemJpaRepository.findById(event.itemId)
            .orElseThrow { Exception("Itemが存在しません") }

        val basketItemJpaEntity = BasketItemJpaEntity(
            basketItemId = ULID().nextULID(),
            basket = basketJpaEntity,
            item = itemJpaEntity,
            itemQuantity = event.itemQuantity.value
        )
    }

    @EventHandler
    fun on(event: ItemDeletedEvent) {
        basketItemJpaRepository.deleteByBasketIdAndItemId(
            basketId = event.id.value,
            itemId = event.itemId
        )
    }

    @EventHandler
    fun on(event: BasketClearedEvent) {
        basketItemJpaRepository.deleteByBasketId(event.id.value)
    }
}