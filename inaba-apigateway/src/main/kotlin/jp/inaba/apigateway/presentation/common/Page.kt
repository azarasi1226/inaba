package jp.inaba.apigateway.presentation.common

data class Page<T>(
    val items: List<T>,
    val paging: Paging,
)
