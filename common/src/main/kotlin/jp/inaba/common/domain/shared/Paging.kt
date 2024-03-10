package jp.inaba.common.domain.shared

data class Paging(
    val totalCount: Int,
    val pageSize: Int,
    val pageNumber: Int
)