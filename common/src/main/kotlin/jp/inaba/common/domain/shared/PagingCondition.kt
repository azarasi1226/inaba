package jp.inaba.common.domain.shared

data class PagingCondition(
    val pageSize: Int,
    val pageNumber: Int,
){
    val offset
        get() = pageNumber * pageSize
}