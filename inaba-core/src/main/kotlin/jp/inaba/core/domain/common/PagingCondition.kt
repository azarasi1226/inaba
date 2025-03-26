package jp.inaba.core.domain.common

data class PagingCondition(
    val pageSize: Int,
    val pageNumber: Int,
) {
    val offset
        get() = (pageNumber - 1) * pageSize

    init {
        if (pageSize <= 0) {
            throw ValueObjectException("pageSizeは[1 ~]の数値を入力してください。pageSize:[$pageSize]")
        }
        if (pageNumber < 1) {
            throw ValueObjectException("pageNumberは[1 ~]の数値を入力してください。pageNumber:[$pageNumber]")
        }
    }
}
