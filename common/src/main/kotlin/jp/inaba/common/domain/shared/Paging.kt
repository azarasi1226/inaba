package jp.inaba.common.domain.shared

data class Paging(
    val totalCount: Long,
    val pageSize: Int,
    val pageNumber: Int,
)
