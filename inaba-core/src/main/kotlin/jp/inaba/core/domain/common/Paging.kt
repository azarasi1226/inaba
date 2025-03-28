package jp.inaba.core.domain.common

data class Paging(
    val totalCount: Long,
    val pageSize: Int,
    val pageNumber: Int,
) {
    init {
        if (totalCount < 0) {
            throw ValueObjectException("totalCountは[0 ~ ]の数値を乳旅行してください。totalCount:[$totalCount]")
        }
        if (pageSize <= 0) {
            throw ValueObjectException("pageSizeは[1 ~]の数値を入力してください。pageSize:[$pageSize]")
        }
        if (pageNumber < 0) {
            throw ValueObjectException("pageNumberは[0 ~]の数値を入力してください。pageNumber:[$pageNumber]")
        }
    }
}
