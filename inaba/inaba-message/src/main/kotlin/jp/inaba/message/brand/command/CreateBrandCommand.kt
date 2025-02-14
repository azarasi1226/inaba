package jp.inaba.message.brand.command

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.brand.BrandName
import org.axonframework.commandhandling.RoutingKey

data class CreateBrandCommand(
    @RoutingKey
    val id: BrandId,
    val name: BrandName,
)