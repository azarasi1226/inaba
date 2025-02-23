package jp.inaba.apigateway.presentation.common

data class SortCondition(
    val property: String,
    val direction: SortDirection,
)
