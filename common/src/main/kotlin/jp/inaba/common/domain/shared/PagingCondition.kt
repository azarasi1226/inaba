package jp.inaba.common.domain.shared

data class PagingCondition(
    val pageSize: Int,
    val pageNumber: Int,
)