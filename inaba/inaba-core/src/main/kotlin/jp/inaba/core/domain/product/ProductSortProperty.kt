package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.SortProperty

enum class ProductSortProperty(
    override val propertyName: String
): SortProperty {
    PRICE("price"),
}