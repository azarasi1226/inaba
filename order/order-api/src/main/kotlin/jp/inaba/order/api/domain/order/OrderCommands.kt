package jp.inaba.order.api.domain.order

import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.modelling.command.TargetAggregateIdentifier

interface OrderCommand {
    @get:TargetAggregateIdentifier
    val id: OrderId
}

data class IssueOrderCommand(
    override val id: OrderId,
    val userId: UserId,
) : OrderCommand

data class CompleteOrderCommand(override val id: OrderId) : OrderCommand

data class FaileOrderCommand(override val id: OrderId) : OrderCommand
