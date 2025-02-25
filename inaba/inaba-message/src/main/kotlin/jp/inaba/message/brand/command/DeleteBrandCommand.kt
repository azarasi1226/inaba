package jp.inaba.message.brand.command

import jp.inaba.core.domain.brand.BrandId

data class DeleteBrandCommand(
    override val id: BrandId
) : BrandAggregateCommand