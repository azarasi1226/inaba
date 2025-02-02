package jp.inaba.service.domain.stock

import jp.inaba.core.domain.common.IdempotencyId
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.stock.IncreaseStockError
import jp.inaba.core.domain.stock.StockId
import jp.inaba.core.domain.stock.StockQuantity
import jp.inaba.message.stock.command.IncreaseStockCommand
import jp.inaba.message.stock.event.StockCreatedEvent
import jp.inaba.message.stock.event.StockIncreasedEvent
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.axonframework.test.matchers.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
                    productId = productId
                )
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                StockCreatedEvent(
                    id = stockId.value,
                    productId = productId.value,
                )
            )
    }

    @Test
    fun `入庫_Event発行`() {
        // Arrange
        val stockId = StockId()
        val productId = ProductId()
        val increaseCount = StockQuantity(1000)
        val idempotencyId = IdempotencyId()

        // Act
        fixture.given(
            StockCreatedEvent(
                id = stockId.value,
                productId = productId.value,
            )
        )
            .`when`(
                IncreaseStockCommand(
                    id = stockId,
                    increaseCount = increaseCount,
                    idempotencyId = idempotencyId,
                )
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                StockIncreasedEvent(
                    id = stockId.value,
                    increaseCount = increaseCount.value,
                    idempotencyId = idempotencyId.value,
                    // 在庫がないのだから入庫した分のはず
                    increasedStockQuantity = 1000
                )
            )
    }

    @Test
    fun `同じ冪等性キーですでに入庫済み_入庫_Eventが発行されない`() {
        // Arrange
        val stockId = StockId()
        val productId = ProductId()
        val increaseCount = StockQuantity(1000)
        val idempotencyId = IdempotencyId()

        // Act
        fixture.given(
            StockCreatedEvent(
                id = stockId.value,
                productId = productId.value,
            ),
            StockIncreasedEvent(
                id = stockId.value,
                increaseCount = increaseCount.value,
                idempotencyId = idempotencyId.value,
                increasedStockQuantity = 1000
            )
        )
            .`when`(
                IncreaseStockCommand(
                    id = stockId,
                    increaseCount = increaseCount,
                    idempotencyId = idempotencyId,
                )
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
        val increaseCount = StockQuantity(1_000_000)
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
                increaseCount = increaseCount.value,
                idempotencyId = idempotencyId1.value,
                increasedStockQuantity = 1_000_000
            )
        )
            .`when`(
                IncreaseStockCommand(
                    id = stockId,
                    increaseCount = StockQuantity(1),
                    idempotencyId = idempotencyId2,
                )
            )
            // Assert
            .expectNoEvents()
            .expectException(
                Matchers.predicate<UseCaseException> {
                    it.error == IncreaseStockError.OutOfStock
                }
            )
    }

    //TODO(残りも実装)
}