package jp.inaba.common.domain.shared

data class PagingCondition(
    val pageSize: Int,
    val pageNumber: Int,
){
    init {
        if(pageSize  <= 0) {
            throw Exception("値がおかしいよ")
        }
        if(pageNumber < 0) {
            throw Exception("値がおかしいよ")
        }
    }
    val offset
        get() = pageNumber * pageSize
}