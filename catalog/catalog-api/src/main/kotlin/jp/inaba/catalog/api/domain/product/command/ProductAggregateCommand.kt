package jp.inaba.catalog.api.domain.product.command

import jp.inaba.catalog.share.domain.product.ProductId
import org.axonframework.modelling.command.TargetAggregateIdentifier

interface ProductAggregateCommand {
    @get:TargetAggregateIdentifier
    val id: ProductId
}
