package jp.inaba.service.domain.order

import jp.inaba.core.domain.order.OrderId
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.product.StockQuantity
import jp.inaba.core.domain.user.UserId
import jp.inaba.message.order.command.CompleteOrderCommand
import jp.inaba.message.order.command.FailOrderCommand
import jp.inaba.message.order.command.IssueOrderCommand
import jp.inaba.message.order.event.OrderCompletedEvent
import jp.inaba.message.order.event.OrderFailedEvent
import jp.inaba.message.order.event.OrderIssuedEvent
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OrderAggregateTest {
    private lateinit var fixture: FixtureConfiguration<OrderAggregate>

    @BeforeEach
    fun before() {
        fixture = AggregateTestFixture(OrderAggregate::class.java)
    }

    @Test
    fun `注文を発行_Event発行`() {
        // Arrange
        val orderId = OrderId()
        val userId = UserId()
        val productId = ProductId()
        val stockQuantity = StockQuantity(5)
        val basketItems =
            listOf(
                IssueOrderCommand.BasketItem(
                    productId = productId,
                    stockQuantity = stockQuantity,
                ),
            )

        fixture
            .givenNoPriorActivity()
            // Act
            .`when`(
                IssueOrderCommand(
                    id = orderId,
                    userId = userId,
                    basketItems = basketItems,
                ),
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                OrderIssuedEvent(
                    id = orderId.value,
                    userId = userId.value,
                    basketItems =
                        listOf(
                            OrderIssuedEvent.BasketItem(
                                productId = productId.value,
                                stockQuantity = stockQuantity.value,
                            ),
                        ),
                ),
            )
    }

    @Test
    fun `注文を完了_Event発行`() {
        // Arrange
        val orderId = OrderId()
        val userId = UserId()
        val productId = ProductId()
        val stockQuantity = StockQuantity(5)

        fixture
            .given(
                OrderIssuedEvent(
                    id = orderId.value,
                    userId = userId.value,
                    basketItems =
                        listOf(
                            OrderIssuedEvent.BasketItem(
                                productId = productId.value,
                                stockQuantity = stockQuantity.value,
                            ),
                        ),
                ),
            )
            // Act
            .`when`(
                CompleteOrderCommand(
                    id = orderId,
                ),
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                OrderCompletedEvent(
                    id = orderId.value,
                ),
            )
    }

    @Test
    fun `注文を失敗_Event発行`() {
        // Arrange
        val orderId = OrderId()
        val userId = UserId()
        val productId = ProductId()
        val stockQuantity = StockQuantity(5)

        fixture
            .given(
                OrderIssuedEvent(
                    id = orderId.value,
                    userId = userId.value,
                    basketItems =
                        listOf(
                            OrderIssuedEvent.BasketItem(
                                productId = productId.value,
                                stockQuantity = stockQuantity.value,
                            ),
                        ),
                ),
            )
            // Act
            .`when`(
                FailOrderCommand(
                    id = orderId,
                ),
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                OrderFailedEvent(
                    id = orderId.value,
                ),
            )
    }
}

