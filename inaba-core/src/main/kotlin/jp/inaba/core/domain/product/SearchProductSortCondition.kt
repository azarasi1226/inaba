package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.SortCondition
import jp.inaba.core.domain.common.SortDirection

enum class SearchProductSortCondition(
    override val dbColumnName: String,
    override val sortDirection: SortDirection,
) : SortCondition {
    PRICE_ASC("price", SortDirection.ASC),
    PRICE_DESC("price", SortDirection.DESC),
    REGISTRATION_DATE_ASC("created_at", SortDirection.ASC),
}
