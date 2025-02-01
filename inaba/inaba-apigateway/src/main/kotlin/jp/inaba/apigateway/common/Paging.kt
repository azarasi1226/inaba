package jp.inaba.apigateway.common

data class Paging(
    val totalCount: Long,
    val pageSize: Int,
    val pageNumber: Int,
)
