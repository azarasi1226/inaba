package jp.inaba.apigateway.presentation.common

data class SortCondition<P : Enum<P>>(
    val property: P,
    val direction: SortDirection,
)
