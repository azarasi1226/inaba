package jp.inaba.service.domain.order

import jp.inaba.order.api.domain.order.IssueOrderCommand
import jp.inaba.order.api.domain.order.OrderId
import jp.inaba.order.api.domain.order.OrderIssuedEvent
import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OrderAggregateTest {
    private lateinit var fixture: AggregateTestFixture<OrderAggregate>

    @BeforeEach
    fun before() {
        fixture = AggregateTestFixture(OrderAggregate::class.java)
    }

    @Test
    fun `OrderAggregateが存在しない_IssueOrderCommandが投げられたら_OrderIssuedEventが発行される`() {
        val orderId = OrderId()

        fixture.givenNoPriorActivity()
            .`when`(IssueOrderCommand(orderId, "", ""))
            .expectEvents(OrderIssuedEvent(orderId, ""))
    }
}
