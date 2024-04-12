package jp.inaba.identity.api.domain.user

object UserErrors {
    enum class FindById(
        val errorCode: String,
        val errorMessage: String
    ) {
        USER_NOT_FOUND("00000", "ユーザーが存在しませんでした")
    }
}