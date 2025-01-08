package jp.inaba.message.user.query

import jp.inaba.core.domain.user.UserId

data class FindUserByIdQuery(
    val userId: UserId,
)

data class FindUserByIdResult(
    val id: String,
    val name: String,
)
