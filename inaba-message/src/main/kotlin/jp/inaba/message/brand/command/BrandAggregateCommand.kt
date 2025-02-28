package jp.inaba.message.brand.command

import jp.inaba.core.domain.brand.BrandId
import org.axonframework.modelling.command.TargetAggregateIdentifier

interface BrandAggregateCommand {
    @get:TargetAggregateIdentifier
    val id: BrandId
}
