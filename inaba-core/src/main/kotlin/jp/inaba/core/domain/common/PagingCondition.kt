package jp.inaba.core.domain.common

data class PagingCondition(
    val pageSize: Int,
    val pageNumber: Int,
) {
    val offset
        get() = pageNumber * pageSize

    init {
        if (pageSize <= 0) {
            throw ValueObjectException("pageSizeは[1 ~]の数値を入力してください。pageSize:[$pageSize]")
        }
        if (pageNumber < 0) {
            throw ValueObjectException("pageNumberは[0 ~]の数値を入力してください。pageNumber:[$pageNumber]")
        }
    }
}
