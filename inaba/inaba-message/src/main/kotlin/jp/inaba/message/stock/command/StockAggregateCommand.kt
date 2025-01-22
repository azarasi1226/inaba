package jp.inaba.message.stock.command

import jp.inaba.core.domain.stock.StockId
import org.axonframework.modelling.command.TargetAggregateIdentifier

interface StockAggregateCommand {
    @get:TargetAggregateIdentifier
    val id: StockId
}