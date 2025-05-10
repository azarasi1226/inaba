package jp.inaba.core.domain.common

interface SortCondition {
    val dbColumnName: String
    val sortDirection: SortDirection
}
