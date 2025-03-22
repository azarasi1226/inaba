package jp.inaba.service.domain.stock

import jp.inaba.core.domain.common.IdempotencyId
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.product.DecreaseStockQuantity
import jp.inaba.core.domain.product.DecreaseStockError
import jp.inaba.core.domain.product.IncreaseStockQuantity
import jp.inaba.core.domain.product.IncreaseStockError
import jp.inaba.core.domain.stock.StockId
import jp.inaba.message.product.command.DecreaseStockCommand
import jp.inaba.message.product.command.IncreaseStockCommand
import jp.inaba.message.stock.event.StockCreatedEvent
import jp.inaba.message.product.event.StockDecreasedEvent
import jp.inaba.message.product.event.StockIncreasedEvent
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.axonframework.test.matchers.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

// TODO(テストの定数が多すぎる気がする。もっとスマートに)
class StockAggregateTest {
    private lateinit var fixture: FixtureConfiguration<StockAggregate>

    @BeforeEach
    fun before() {
        fixture = AggregateTestFixture(StockAggregate::class.java)
    }

    @Test
    fun `在庫を作成_Event発行`() {
        // Arrange
        val stockId = StockId()
        val productId = ProductId()

        // Act
        fixture.givenNoPriorActivity()
            .`when`(
                InternalCreateStockCommand(
                    id = stockId,
                    productId = productId,
                ),
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                StockCreatedEvent(
                    id = stockId.value,
                    productId = productId.value,
                ),
            )
    }

    @Test
    fun `入庫_Event発行`() {
        // Arrange
        val stockId = StockId()
        val productId = ProductId()
        val increaseCount = IncreaseStockQuantity(1000)
        val idempotencyId = IdempotencyId()

        // Act
        fixture.given(
            StockCreatedEvent(
                id = stockId.value,
                productId = productId.value,
            ),
        )
            .`when`(
                IncreaseStockCommand(
                    id = stockId,
                    increaseStockQuantity = increaseCount,
                    idempotencyId = idempotencyId,
                ),
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                StockIncreasedEvent(
                    id = stockId.value,
                    increaseStockQuantity = increaseCount.value,
                    idempotencyId = idempotencyId.value,
                    // 在庫がないのだから入庫した分のはず
                    increasedStockQuantity = 1000,
                ),
            )
    }

    @Test
    fun `同じ冪等性キーですでに入庫済み_入庫_Eventが発行されない`() {
        // Arrange
        val stockId = StockId()
        val productId = ProductId()
        val increaseCount = IncreaseStockQuantity(1000)
        val idempotencyId = IdempotencyId()

        // Act
        fixture.given(
            StockCreatedEvent(
                id = stockId.value,
                productId = productId.value,
            ),
            StockIncreasedEvent(
                id = stockId.value,
                increaseStockQuantity = increaseCount.value,
                idempotencyId = idempotencyId.value,
                increasedStockQuantity = 1000,
            ),
        )
            .`when`(
                IncreaseStockCommand(
                    id = stockId,
                    increaseStockQuantity = increaseCount,
                    idempotencyId = idempotencyId,
                ),
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectNoEvents()
    }

    @Test
    fun `入庫数が限界に達している_入庫_Eventが発行されずException`() {
        // Arrange
        val stockId = StockId()
        val productId = ProductId()
        val increaseCount = IncreaseStockQuantity(1_000_000)
        val idempotencyId1 = IdempotencyId()
        val idempotencyId2 = IdempotencyId()

        // Act
        fixture.given(
            StockCreatedEvent(
                id = stockId.value,
                productId = productId.value,
            ),
            StockIncreasedEvent(
                id = stockId.value,
                increaseStockQuantity = increaseCount.value,
                idempotencyId = idempotencyId1.value,
                increasedStockQuantity = 1_000_000,
            ),
        )
            .`when`(
                IncreaseStockCommand(
                    id = stockId,
                    increaseStockQuantity = IncreaseStockQuantity(1),
                    idempotencyId = idempotencyId2,
                ),
            )
            // Assert
            .expectNoEvents()
            .expectException(
                Matchers.predicate<UseCaseException> {
                    it.error == IncreaseStockError.CANNOT_RECEIVE
                },
            )
    }

    @Test
    fun `出庫_Event発行`() {
        // Arrange
        val stockId = StockId()
        val productId = ProductId()
        val decreaseCount = DecreaseStockQuantity(1)
        val idempotencyId = IdempotencyId()

        // Act
        fixture.given(
            StockCreatedEvent(
                id = stockId.value,
                productId = productId.value,
            ),
            StockIncreasedEvent(
                id = stockId.value,
                increaseStockQuantity = 1,
                idempotencyId = IdempotencyId().value,
                increasedStockQuantity = 1,
            ),
        )
            .`when`(
                DecreaseStockCommand(
                    id = stockId,
                    decreaseStockQuantity = decreaseCount,
                    idempotencyId = idempotencyId,
                ),
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                StockDecreasedEvent(
                    id = stockId.value,
                    decreaseStockQuantity = decreaseCount.value,
                    idempotencyId = idempotencyId.value,
                    decreasedStockQuantity = 0,
                ),
            )
    }

    @Test
    fun `同じ冪等性キーですでに出庫済み_出庫_Eventが発行されない`() {
        // Arrange
        val stockId = StockId()
        val productId = ProductId()
        val decreaseCount = DecreaseStockQuantity(1000)
        val idempotencyId = IdempotencyId()

        // Act
        fixture.given(
            StockCreatedEvent(
                id = stockId.value,
                productId = productId.value,
            ),
            StockIncreasedEvent(
                id = stockId.value,
                increaseStockQuantity = 100,
                idempotencyId = IdempotencyId().value,
                increasedStockQuantity = 100,
            ),
            StockDecreasedEvent(
                id = stockId.value,
                decreaseStockQuantity = 10,
                idempotencyId = idempotencyId.value,
                decreasedStockQuantity = 90,
            ),
        )
            .`when`(
                DecreaseStockCommand(
                    id = stockId,
                    decreaseStockQuantity = decreaseCount,
                    idempotencyId = idempotencyId,
                ),
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectNoEvents()
    }

    @Test
    fun `在庫はある_在庫以上の数を出庫_Eventが発行されずException`() {
        // Arrange
        val stockId = StockId()
        val productId = ProductId()
        val decreaseCount = DecreaseStockQuantity(1_000_000)
        val idempotencyId1 = IdempotencyId()
        val idempotencyId2 = IdempotencyId()

        // Act
        fixture.given(
            StockCreatedEvent(
                id = stockId.value,
                productId = productId.value,
            ),
        )
            .`when`(
                DecreaseStockCommand(
                    id = stockId,
                    decreaseStockQuantity = decreaseCount,
                    idempotencyId = idempotencyId2,
                ),
            )
            // Assert
            .expectNoEvents()
            .expectException(
                Matchers.predicate<UseCaseException> {
                    it.error == DecreaseStockError.INSUFFICIENT_STOCK
                },
            )
    }
}
