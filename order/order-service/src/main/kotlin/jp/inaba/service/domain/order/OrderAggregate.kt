package jp.inaba.service.domain.order

import jp.inaba.order.api.domain.order.CompleteOrderCommand
import jp.inaba.order.api.domain.order.FailOrderCommand
import jp.inaba.order.api.domain.order.IssueOrderCommand
import jp.inaba.order.api.domain.order.OrderCompletedEvent
import jp.inaba.order.api.domain.order.OrderFailedEvent
import jp.inaba.order.api.domain.order.OrderId
import jp.inaba.order.api.domain.order.OrderIssuedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class OrderAggregate() {
    @AggregateIdentifier
    private lateinit var id: OrderId
    private lateinit var status: OrderStatus

    @CommandHandler
    constructor(command: IssueOrderCommand) : this() {
        val event =
            OrderIssuedEvent(
                id = command.id.value,
                userId = command.userId.value,
                basketItems = command.basketItems.map{
                    OrderIssuedEvent.BasketItem(
                        productId = it.productId.value,
                        productQuantity = it.productQuantity.value
                    )
                }
            )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: CompleteOrderCommand) {
        val event = OrderCompletedEvent(command.id.value)

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: FailOrderCommand) {
        val event = OrderFailedEvent(command.id.value)

        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: OrderIssuedEvent) {
        val orderId = OrderId(event.id)

        id = orderId
        status = OrderStatus.Issued
    }



    @EventSourcingHandler
    fun on(event: OrderCompletedEvent) {
        status = OrderStatus.Completed
    }

    @EventSourcingHandler
    fun on(event: OrderFailedEvent) {
        status = OrderStatus.Failed
    }
}
