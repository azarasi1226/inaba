package jp.inaba.apigateway.common

data class PagingCondition(
    val pageSize: Int,
    val pageNumber: Int,
)