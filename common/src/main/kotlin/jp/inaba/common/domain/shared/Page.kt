package jp.inaba.common.domain.shared

data class Page<T>(
    val items: List<T>,
    val paging: Paging,
)
