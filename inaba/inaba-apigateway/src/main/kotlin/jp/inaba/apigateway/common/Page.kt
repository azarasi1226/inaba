package jp.inaba.apigateway.common

data class Page<T>(
    val items: List<T>,
    val paging: Paging,
)
