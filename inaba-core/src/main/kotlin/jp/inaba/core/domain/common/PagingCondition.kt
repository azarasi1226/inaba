package jp.inaba.core.domain.common

data class PagingCondition(
    val pageSize: Int,
    val pageNumber: Int,
) {
    val offset
        get() = (pageNumber - 1) * pageSize

    init {
        if (pageSize < 1) {
            throw ValueObjectException("pageSizeは[1 ~]な数値を入力してください。pageSize:[$pageSize]")
        }
        if (pageNumber < 1) {
            throw ValueObjectException("pageNumberは[1 ~]な数値を入力してください。pageNumber:[$pageNumber]")
        }
    }
}
