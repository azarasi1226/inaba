package jp.inaba.message.order.command

import jp.inaba.core.domain.order.OrderId
import org.axonframework.modelling.command.TargetAggregateIdentifier

interface OrderAggregateCommand {
    @get:TargetAggregateIdentifier
    val id: OrderId
}