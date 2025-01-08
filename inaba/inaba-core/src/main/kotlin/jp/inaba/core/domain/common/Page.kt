package jp.inaba.core.domain.common

data class Page<T>(
    val items: List<T>,
    val paging: Paging,
)
