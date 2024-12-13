package jp.inaba.identity.api.domain.user.query

import jp.inaba.identity.share.domain.user.UserId

data class FindUserByIdQuery(
    val userId: UserId,
)

data class FindUserByIdResult(
    val id: String,
    val name: String,
)
