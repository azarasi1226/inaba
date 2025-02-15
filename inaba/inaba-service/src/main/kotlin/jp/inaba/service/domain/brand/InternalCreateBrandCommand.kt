package jp.inaba.service.domain.brand

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.brand.BrandName
import jp.inaba.message.brand.command.BrandAggregateCommand

data class InternalCreateBrandCommand(
    override val id: BrandId,
    val name: BrandName,
) : BrandAggregateCommand
