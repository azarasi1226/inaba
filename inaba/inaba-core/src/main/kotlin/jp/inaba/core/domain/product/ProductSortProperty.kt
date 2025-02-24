package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.SortProperty

enum class ProductSortProperty(
    override val dbColumnName: String,
) : SortProperty {
    PRICE("price"),
    REGISTRATION_DATE("created_at")
}
