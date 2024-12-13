package jp.inaba.basket.service.domain.basket

import jp.inaba.basket.api.domain.basket.command.ClearBasketCommand
import jp.inaba.basket.api.domain.basket.command.DeleteBasketItemCommand
import jp.inaba.basket.api.domain.basket.event.BasketClearedEvent
import jp.inaba.basket.api.domain.basket.event.BasketCreatedEvent
import jp.inaba.basket.api.domain.basket.event.BasketItemDeletedEvent
import jp.inaba.basket.api.domain.basket.event.BasketItemSetEvent
import jp.inaba.basket.share.domain.basket.BasketId
import jp.inaba.basket.share.domain.basket.BasketItemQuantity
import jp.inaba.basket.share.domain.basket.SetBasketItemError
import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.common.domain.shared.ActionCommandResult
import jp.inaba.identity.share.domain.user.UserId
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
    fun `買い物かごを作成_買い物かごが作成されたイベント発行`() {
        val basketId = BasketId()
        val userId = UserId()

        fixture.givenNoPriorActivity()
            .`when`(
                InternalCreateBasketCommand(
                    id = basketId,
                    userId = userId,
                ),
            )
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                BasketCreatedEvent(
                    id = basketId.value,
                    userId = userId.value,
                ),
            )
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 49])
    fun `アイテムをセットする_アイテムがセットされたイベント発行`(eventCount: Int) {
        val basketId = BasketId()
        val userId = UserId()
        val productId = ProductId()
        val quantity = BasketItemQuantity(20)
        val itemSetEvents =
            (1..eventCount).map {
                BasketItemSetEvent(
                    id = basketId.value,
                    productId = ProductId().value,
                    basketItemQuantity = quantity.value,
                )
            }

        fixture.given(
            BasketCreatedEvent(
                id = basketId.value,
                userId = userId.value,
            ),
            *itemSetEvents.toTypedArray(),
        )
            .`when`(
                InternalSetBasketItemCommand(
                    id = basketId,
                    productId = productId,
                    basketItemQuantity = quantity,
                ),
            )
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                BasketItemSetEvent(
                    id = basketId.value,
                    productId = productId.value,
                    basketItemQuantity = quantity.value,
                ),
            )
            .expectResultMessagePayload(
                ActionCommandResult.ok(),
            )
    }

    @Test
    fun `アイテムが50種類買い物かごに入っている_アイテムをセットする_エラー返却`() {
        val basketId = BasketId()
        val userId = UserId()
        val productId = ProductId()
        val quantity = BasketItemQuantity(20)
        val itemSetEvents =
            (1..50).map {
                BasketItemSetEvent(
                    id = basketId.value,
                    productId = ProductId().value,
                    basketItemQuantity = quantity.value,
                )
            }

        fixture.given(
            BasketCreatedEvent(
                id = basketId.value,
                userId = userId.value,
            ),
            *itemSetEvents.toTypedArray(),
        )
            .`when`(
                InternalSetBasketItemCommand(
                    id = basketId,
                    productId = productId,
                    basketItemQuantity = quantity,
                ),
            )
            .expectNoEvents()
            .expectResultMessagePayload(
                ActionCommandResult.error(SetBasketItemError.PRODUCT_MAX_KIND_OVER.errorCode),
            )
    }

    @Test
    fun `削除対象のアイテムが存在する_アイテムを削除する_アイテムを削除したイベント発行`() {
        val basketId = BasketId()
        val userId = UserId()
        val productId = ProductId()
        val quantity = BasketItemQuantity(20)

        fixture.given(
            BasketCreatedEvent(
                id = basketId.value,
                userId = userId.value,
            ),
            BasketItemSetEvent(
                id = basketId.value,
                productId = productId.value,
                basketItemQuantity = quantity.value,
            ),
        )
            .`when`(
                DeleteBasketItemCommand(
                    id = basketId,
                    productId = productId,
                ),
            )
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                BasketItemDeletedEvent(
                    id = basketId.value,
                    productId = productId.value,
                ),
            )
    }

    @Test
    fun `削除対象のアイテムが存在しない_アイテムを削除する_アイテムを削除したイベント発行`() {
        val basketId = BasketId()
        val userId = UserId()
        val productId = ProductId()

        fixture.given(
            BasketCreatedEvent(
                id = basketId.value,
                userId = userId.value,
            ),
        )
            .`when`(
                DeleteBasketItemCommand(
                    id = basketId,
                    productId = productId,
                ),
            )
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                BasketItemDeletedEvent(
                    id = basketId.value,
                    productId = productId.value,
                ),
            )
    }

    @Test
    fun `アイテムが存在する_買い物かごクリア_買い物かごがクリアされたイベント発行`() {
        val basketId = BasketId()
        val userId = UserId()
        val productId = ProductId()
        val quantity = BasketItemQuantity(20)

        fixture.given(
            BasketCreatedEvent(
                id = basketId.value,
                userId = userId.value,
            ),
            BasketItemSetEvent(
                id = basketId.value,
                productId = productId.value,
                basketItemQuantity = quantity.value,
            ),
        )
            .`when`(
                ClearBasketCommand(basketId),
            )
            .expectSuccessfulHandlerExecution()
            .expectEvents(BasketClearedEvent(basketId.value))
    }

    @Test
    fun `アイテムが存在しない_買い物かごクリア_買い物かごがクリアされたイベント発行`() {
        val basketId = BasketId()
        val userId = UserId()

        fixture.given(
            BasketCreatedEvent(
                id = basketId.value,
                userId = userId.value,
            ),
        )
            .`when`(
                ClearBasketCommand(basketId),
            )
            .expectSuccessfulHandlerExecution()
            .expectEvents(BasketClearedEvent(basketId.value))
    }
}
