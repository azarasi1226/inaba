package jp.inaba.basket.api.domain.basket.command

import jp.inaba.basket.share.domain.basket.BasketId
import org.axonframework.modelling.command.TargetAggregateIdentifier

interface BasketAggregateCommand {
    @get:TargetAggregateIdentifier
    val id: BasketId
}