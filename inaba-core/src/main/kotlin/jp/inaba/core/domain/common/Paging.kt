package jp.inaba.core.domain.common

import kotlin.math.ceil

data class Paging(
    val totalCount: Long,
    val pageSize: Int,
    val pageNumber: Int,
) {
    val totalPage: Int = ceil(totalCount.toDouble() / pageSize).toInt()

    init {
        if (totalCount < 0) {
            throw ValueObjectException("totalCountは[0 ~]な数値を入力してください。totalCount:[$totalCount]")
        }
        if (pageSize < 1) {
            throw ValueObjectException("pageSizeは[1 ~]な数値を入力してください。pageSize:[$pageSize]")
        }
        if (pageNumber < 1) {
            throw ValueObjectException("pageNumberは[1 ~]な数値を入力してください。pageNumber:[$pageNumber]")
        }
    }
}
