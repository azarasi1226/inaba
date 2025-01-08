package jp.inaba.core.domain.common

data class Paging(
    val totalCount: Long,
    val pageSize: Int,
    val pageNumber: Int,
)
