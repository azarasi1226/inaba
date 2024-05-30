package jp.inaba.service.domain.order

import jp.inaba.order.api.domain.order.CompleteOrderCommand
import jp.inaba.order.api.domain.order.FaileOrderCommand
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
                id = command.id,
                userId = command.userId,
            )

        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: OrderIssuedEvent) {
        id = event.id
        status = OrderStatus.Issued
    }

    @CommandHandler
    fun handle(command: CompleteOrderCommand) {
        val event = OrderCompletedEvent(command.id)

        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: OrderCompletedEvent) {
        status = OrderStatus.Completed
    }

    @CommandHandler
    fun handle(command: FaileOrderCommand) {
        val event = OrderFailedEvent(command.id)

        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: OrderFailedEvent) {
        status = OrderStatus.Failed
    }
}
