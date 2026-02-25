package jp.inaba.message.product.command

import jp.inaba.core.domain.product.ProductId
import org.axonframework.modelling.annotation.TargetEntityId

interface ProductCommand {
    @get:TargetEntityId
    val id: ProductId
}
