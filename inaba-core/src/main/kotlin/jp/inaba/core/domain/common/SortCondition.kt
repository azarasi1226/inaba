package jp.inaba.core.domain.common

data class SortCondition<T : SortProperty>(
    val property: T,
    val direction: SortDirection,
)
