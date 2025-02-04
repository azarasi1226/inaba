package jp.inaba.message.auth.query

data class GetAuthUserQuery(
    val emailAddress: String
)

data class GetAuthUserResult(
    val subject: String
)