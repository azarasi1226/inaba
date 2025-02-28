package jp.inaba.message.brand.query

import jp.inaba.core.domain.brand.BrandId

data class FindBrandByIdQuery(
    val id: BrandId,
)

data class FindBrandByIdResult(
    val name: String,
)
