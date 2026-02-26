package jp.inaba.core.domain.common

import com.fasterxml.jackson.annotation.JsonIgnore

data class PagingCondition(
    val pageSize: Int,
    val pageNumber: Int,
) {
    //TODO: なぜここだけ@JsonIgnoreをつけないとエラーが出るのか不明、調査するべし
    @get:JsonIgnore
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
