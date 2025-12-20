package jp.inaba.service.domain.basket

import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.basket.BasketItemQuantity
import jp.inaba.core.domain.basket.SetBasketItemError
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.user.UserId
import jp.inaba.message.basket.command.ClearBasketCommand
import jp.inaba.message.basket.command.DeleteBasketItemCommand
import jp.inaba.message.basket.event.BasketClearedEvent
import jp.inaba.message.basket.event.BasketCreatedEvent
import jp.inaba.message.basket.event.BasketItemDeletedEvent
import jp.inaba.message.basket.event.BasketItemSetEvent
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.axonframework.test.matchers.Matchers
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
    fun `買い物かごを作成_Event発行`() {
        // Arrange
        val basketId = BasketId()
        val userId = UserId()

        fixture
            .givenNoPriorActivity()
            // Act
            .`when`(
                InternalCreateBasketCommand(
                    id = basketId,
                    userId = userId,
                ),
            )
            // Assert
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
    fun `アイテムが50種類以下買い物かごに入っている_アイテムをセット_Event発行`(itemKindCount: Int) {
        // Arrange
        val basketId = BasketId()
        val userId = UserId()
        val productId = ProductId()
        val quantity = BasketItemQuantity(20)

        fixture
            .given(
                BasketCreatedEvent(
                    id = basketId.value,
                    userId = userId.value,
                ),
                *(1..itemKindCount)
                    .map {
                        BasketItemSetEvent(
                            id = basketId.value,
                            productId = ProductId().value,
                            basketItemQuantity = quantity.value,
                        )
                    }.toTypedArray(),
            )
            // Act
            .`when`(
                InternalSetBasketItemCommand(
                    id = basketId,
                    productId = productId,
                    basketItemQuantity = quantity,
                ),
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                BasketItemSetEvent(
                    id = basketId.value,
                    productId = productId.value,
                    basketItemQuantity = quantity.value,
                ),
            )
    }

    @Test
    fun `アイテムが50種類買い物かごに入っている_アイテムをセット_Eventは発行されずException`() {
        // Arrange
        val basketId = BasketId()
        val userId = UserId()
        val productId = ProductId()
        val quantity = BasketItemQuantity(20)
        val itemKindCount = 50

        fixture
            .given(
                BasketCreatedEvent(
                    id = basketId.value,
                    userId = userId.value,
                ),
                *(1..itemKindCount)
                    .map {
                        BasketItemSetEvent(
                            id = basketId.value,
                            productId = ProductId().value,
                            basketItemQuantity = quantity.value,
                        )
                    }.toTypedArray(),
            )
            // Act
            .`when`(
                InternalSetBasketItemCommand(
                    id = basketId,
                    productId = productId,
                    basketItemQuantity = quantity,
                ),
            )
            // Assert
            .expectNoEvents()
            .expectException(
                Matchers.predicate<UseCaseException> {
                    it.error == SetBasketItemError.PRODUCT_MAX_KIND_OVER
                },
            )
    }

    @Test
    fun `削除対象のアイテムが存在_アイテムを削除_Event発行`() {
        // Arrange
        val basketId = BasketId()
        val userId = UserId()
        val productId = ProductId()
        val quantity = BasketItemQuantity(20)

        fixture
            .given(
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
            // Act
            .`when`(
                DeleteBasketItemCommand(
                    id = basketId,
                    productId = productId,
                ),
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                BasketItemDeletedEvent(
                    id = basketId.value,
                    productId = productId.value,
                ),
            )
    }

    @Test
    fun `削除対象のアイテムが存在しない_アイテムを削除する_Event発行`() {
        // Arrange
        val basketId = BasketId()
        val userId = UserId()
        val productId = ProductId()

        fixture
            .given(
                BasketCreatedEvent(
                    id = basketId.value,
                    userId = userId.value,
                ),
            )
            // Act
            .`when`(
                DeleteBasketItemCommand(
                    id = basketId,
                    productId = productId,
                ),
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                BasketItemDeletedEvent(
                    id = basketId.value,
                    productId = productId.value,
                ),
            )
    }

    @Test
    fun `アイテムが存在_買い物かごクリア_Event発行`() {
        // Arrange
        val basketId = BasketId()
        val userId = UserId()
        val productId = ProductId()
        val quantity = BasketItemQuantity(20)

        fixture
            .given(
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
            // Act
            .`when`(
                ClearBasketCommand(basketId),
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectEvents(BasketClearedEvent(basketId.value))
    }
}
