package jp.inaba.basket.service.domain.basket

import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.api.domain.basket.BasketEvents
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.api.domain.basket.BasketItemQuantity
import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.common.domain.shared.DomainException
import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class BasketAggregateTest {
    private lateinit var fixture: FixtureConfiguration<BasketAggregate>

    @BeforeEach
    fun before() {
        fixture = AggregateTestFixture(BasketAggregate::class.java)
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 49])
    fun アイテムをセットする_アイテムがセットされたイベント発行(eventCount: Int) {
        val basketId = BasketId(UserId())
        val productId = ProductId()
        val quantity = BasketItemQuantity(20)
        val itemSetEvents = (1..eventCount).map {
            BasketEvents.BasketItemSet(
                id = basketId.value,
                productId = ProductId().value,
                basketItemQuantity = quantity.value)
        }

        fixture.given(*itemSetEvents.toTypedArray())
            .`when`(
                BasketCommands.SetBasketItem(
                    id = basketId,
                    productId = productId,
                    basketItemQuantity = quantity
                )
            )
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                BasketEvents.BasketItemSet(
                    id = basketId.value,
                    productId = productId.value,
                    basketItemQuantity = quantity.value
                )
            )
    }

    @Test
    fun アイテムをセットされている_全く同じ情報でサイドアイテムをセットする_何も起きない() {
        val basketId = BasketId(UserId())
        val productId = ProductId()
        val quantity = BasketItemQuantity(20)

        fixture.given(
            BasketEvents.BasketItemSet(
                id = basketId.value,
                productId = productId.value,
                basketItemQuantity = quantity.value
            )
        )
            .`when`(
                BasketCommands.SetBasketItem(
                    id = basketId,
                    productId = productId,
                    basketItemQuantity = quantity
                )
            )
            .expectSuccessfulHandlerExecution()
            .expectNoEvents()
    }

    @Test
    fun アイテムが50種類買い物かごに入っている_アイテムをセットする_例外() {
        val basketId = BasketId(UserId())
        val productId = ProductId()
        val quantity = BasketItemQuantity(20)
        val itemSetEvents = (1..50).map {
            BasketEvents.BasketItemSet(
                id = basketId.value,
                productId = ProductId().value,
                basketItemQuantity = quantity.value)
        }

        fixture.given(*itemSetEvents.toTypedArray())
            .`when`(
                BasketCommands.SetBasketItem(
                    id = basketId,
                    productId = productId,
                    basketItemQuantity = quantity
                )
            )
            .expectException(DomainException::class.java)
    }

    @Test
    fun 削除対象のアイテムが存在する_アイテムを削除する_アイテムを削除したイベント発行() {
        val basketId = BasketId(UserId())
        val productId = ProductId()
        val quantity = BasketItemQuantity(20)

        fixture.given(
                BasketEvents.BasketItemSet(
                    id = basketId.value,
                    productId = productId.value,
                    basketItemQuantity = quantity.value
                )
        )
            .`when`(
                BasketCommands.DeleteBasketItem(
                    id = basketId,
                    productId = productId
                )
            )
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                BasketEvents.BasketItemDeleted(
                    id = basketId.value,
                    productId = productId.value
                )
            )
    }

    @Test
    fun 被削除対象のアイテムが存在する_アイテムを削除する_何も起きない() {
        val basketId = BasketId(UserId())
        val productId1 = ProductId()
        val productId2 = ProductId()
        val quantity = BasketItemQuantity(20)

        fixture.given(
            BasketEvents.BasketItemSet(
                id = basketId.value,
                productId = productId1.value,
                basketItemQuantity = quantity.value
            )
        )
            .`when`(
                BasketCommands.DeleteBasketItem(
                    id = basketId,
                    productId = productId2
                )
            )
            .expectSuccessfulHandlerExecution()
            .expectNoEvents()
    }
}