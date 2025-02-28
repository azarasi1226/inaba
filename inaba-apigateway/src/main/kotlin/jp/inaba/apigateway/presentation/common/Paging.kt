package jp.inaba.apigateway.presentation.common

data class Paging(
    val totalCount: Long,
    val pageSize: Int,
    val pageNumber: Int,
)
