package jp.inaba.basket.service.domain.basket

import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.api.domain.basket.BasketEvents
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.api.domain.basket.BasketItemQuantity
import jp.inaba.catalog.api.domain.product.ProductId
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BasketAggregateTest {
    private lateinit var fixture: FixtureConfiguration<BasketAggregate>

    @BeforeEach
    fun before() {
        fixture = AggregateTestFixture(BasketAggregate::class.java)
    }

    @Test
    fun 買い物かご作成_買い物かごが作成されたイベント発行() {
        val basketId = BasketId()
        val userId = "seal1226"
        val command = BasketCommands.Create(id = basketId, userId = userId)

        fixture.givenNoPriorActivity()
            .`when`(command)
            .expectSuccessfulHandlerExecution()
            .expectEvents(BasketEvents.Created(id = basketId.value, userId = userId))
    }

    @Test
    fun アイテムをセットする_アイテムがセットされたイベント発行() {
        val basketId = BasketId()
        val userId = "seal1226"
        val productId = ProductId()
        val quantity = BasketItemQuantity(20)

        fixture.given(BasketEvents.Created(id = basketId.value, userId = userId))
            .`when`(BasketCommands.SetBasketItem(id = basketId, productId = productId, basketItemQuantity = quantity))
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                BasketEvents.BasketItemSet(id = basketId.value, productId = productId.value, basketItemQuantity = quantity.value)
            )
    }

    @Test
    fun すでにアイテムが49種類買い物かごにある_アイテムをセットする_アイテムがセットされたイベント発行() {
        val basketId = BasketId()
        val userId = "seal1226"
        val productId = ProductId()
        val quantity = BasketItemQuantity(20)
        val itemSetEvents = (1..49).map {
            BasketEvents.BasketItemSet(
                id = basketId.value,
                productId = ProductId().value,
                basketItemQuantity = quantity.value)
        }

        fixture.given(
            BasketEvents.Created(id = basketId.value, userId = userId),
            *itemSetEvents.toTypedArray(),
        )
            .`when`(BasketCommands.SetBasketItem(id = basketId, productId = productId, basketItemQuantity = quantity))
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                BasketEvents.BasketItemSet(id = basketId.value, productId = productId.value, basketItemQuantity = quantity.value)
            )
    }


    @Test
    fun すでにアイテムが50種類買い物かごにある_アイテムをセットする_例外() {
        val basketId = BasketId()
        val userId = "seal1226"
        val productId = ProductId()
        val quantity = BasketItemQuantity(20)
        val itemSetEvents = (1..50).map {
            BasketEvents.BasketItemSet(
                id = basketId.value,
                productId = ProductId().value,
                basketItemQuantity = quantity.value)
        }

        fixture.given(
            BasketEvents.Created(id = basketId.value, userId = userId),
            *itemSetEvents.toTypedArray(),
        )
            .`when`(BasketCommands.SetBasketItem(id = basketId, productId = productId, basketItemQuantity = quantity))
            .expectException(Exception::class.java)
    }

    @Test
    fun アイテムが存在する_アイテムを削除する_アイテムを削除したイベント発行() {
        val basketId = BasketId()
        val userId = "seal1226"
        val productId = ProductId()
        val quantity = BasketItemQuantity(20)

        fixture.given(
            BasketEvents.Created(id = basketId.value, userId = userId),
            BasketEvents.BasketItemSet(id = basketId.value, productId = productId.value, basketItemQuantity = quantity.value)
        )
            .`when`(BasketCommands.DeleteBasketItem(id = basketId, productId = productId))
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                BasketEvents.BasketItemDeleted(id = basketId.value, productId = productId.value)
            )
    }

    @Test
    fun アイテムが存在する_存在しないアイテムを削除する_何もしない() {
        val basketId = BasketId()
        val userId = "seal1226"
        val productId1 = ProductId()
        val productId2 = ProductId()
        val quantity = BasketItemQuantity(20)

        fixture.given(
            BasketEvents.Created(id = basketId.value, userId = userId),
            BasketCommands.SetBasketItem(id = basketId, productId = productId1, basketItemQuantity = quantity)
        )
            .`when`(BasketCommands.DeleteBasketItem(id = basketId, productId = productId2))
            .expectSuccessfulHandlerExecution()
            .expectNoEvents()
    }
}