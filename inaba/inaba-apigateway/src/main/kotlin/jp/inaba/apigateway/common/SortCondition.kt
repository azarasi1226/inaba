package jp.inaba.apigateway.common

data class SortCondition(
    val property: String,
    val direction: SortDirection,
)
