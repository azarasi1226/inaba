package jp.inaba.message.brand.command

import jp.inaba.core.domain.brand.BrandId
import org.axonframework.modelling.annotation.TargetEntityId

interface BrandCommand {
    @get:TargetEntityId
    val id: BrandId
}
