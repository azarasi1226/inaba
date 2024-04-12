package jp.inaba.basket.service.domain.basket

import jp.inaba.basket.api.domain.basket.*
import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.common.domain.shared.ActionCommandResult
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

    @Test
    fun バスケットを作成する_バスケットが作成されたイベント発行() {
        val basketId = BasketId(UserId())

        fixture.givenNoPriorActivity()
            .`when`(
                InternalBasketCommands.Create(basketId)
            )
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                BasketEvents.Created(basketId.value)
            )
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

        fixture.given(
                BasketEvents.Created(basketId.value),
                *itemSetEvents.toTypedArray())
            .`when`(
                InternalBasketCommands.SetBasketItem(
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

        fixture.given(
            BasketEvents.Created(basketId.value),
            *itemSetEvents.toTypedArray()
        )
            .`when`(
                InternalBasketCommands.SetBasketItem(
                    id = basketId,
                    productId = productId,
                    basketItemQuantity = quantity
                )
            )
            .expectNoEvents()
            .expectResultMessagePayload(
                ActionCommandResult.error(BasketErrors.SetBasketItem.PRODUCT_MAX_KIND_OVER.errorCode)
            )
    }

    @Test
    fun 削除対象のアイテムが存在する_アイテムを削除する_アイテムを削除したイベント発行() {
        val basketId = BasketId(UserId())
        val productId = ProductId()
        val quantity = BasketItemQuantity(20)

        fixture.given(
            BasketEvents.Created(basketId.value),
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
    fun 削除対象のアイテムが存在しない_アイテムを削除する_アイテムを削除したイベント発行() {
        val basketId = BasketId(UserId())
        val productId = ProductId()

        fixture.given(
            BasketEvents.Created(basketId.value)
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
    fun アイテムが存在する_買い物かごクリア_買い物かごがクリアされたイベント発行() {
        val basketId = BasketId(UserId())
        val productId = ProductId()
        val quantity = BasketItemQuantity(20)

        fixture.given(
            BasketEvents.Created(basketId.value),
            BasketEvents.BasketItemSet(
                id = basketId.value,
                productId = productId.value,
                basketItemQuantity = quantity.value
            )
        )
            .`when`(
                BasketCommands.Clear(basketId)
            )
            .expectSuccessfulHandlerExecution()
            .expectEvents(BasketEvents.Cleared(basketId.value))
    }

    @Test
    fun アイテムが存在しない_買い物かごクリア_買い物かごがクリアされたイベント発行() {
        val basketId = BasketId(UserId())

        fixture.given(
            BasketEvents.Created(basketId.value)
        )
            .`when`(
                BasketCommands.Clear(basketId)
            )
            .expectSuccessfulHandlerExecution()
            .expectEvents(BasketEvents.Cleared(basketId.value))
    }
}