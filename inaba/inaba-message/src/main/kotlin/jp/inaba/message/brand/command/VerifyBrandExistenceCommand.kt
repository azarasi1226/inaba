package jp.inaba.message.brand.command

import jp.inaba.core.domain.brand.BrandId


data class VerifyBrandExistenceCommand(
    override val id: BrandId
) : BrandAggregateCommand