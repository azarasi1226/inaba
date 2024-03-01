package jp.inaba.order.api.domain.order

import org.axonframework.modelling.command.TargetAggregateIdentifier

interface OrderCommand {
    @get:TargetAggregateIdentifier
    val id: OrderId
}

data class IssueOrderCommand(
    override val id: OrderId,
    val userId: String,
    val productId: String
): OrderCommand

data class CompleteOrderCommand(override val id: OrderId) : OrderCommand

data class FaileOrderCommand(override val id: OrderId): OrderCommand
