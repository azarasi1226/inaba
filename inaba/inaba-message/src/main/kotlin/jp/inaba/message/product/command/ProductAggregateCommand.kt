package jp.inaba.message.product.command

import jp.inaba.core.domain.product.ProductId
import org.axonframework.modelling.command.TargetAggregateIdentifier

interface ProductAggregateCommand {
    @get:TargetAggregateIdentifier
    val id: ProductId
}
