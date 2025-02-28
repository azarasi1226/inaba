package jp.inaba.message.basket.command

import jp.inaba.core.domain.basket.BasketId
import org.axonframework.modelling.command.TargetAggregateIdentifier

interface BasketAggregateCommand {
    @get:TargetAggregateIdentifier
    val id: BasketId
}
