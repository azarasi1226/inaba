package jp.inaba.message.order.command

import jp.inaba.core.domain.order.OrderId

data class CompleteOrderCommand(
    override val id: OrderId,
) : OrderAggregateCommand
