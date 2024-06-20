package jp.inaba.identity.api.domain.user

data class FindUserByIdQuery(
    val userId: UserId,
)

data class FindUserByIdResult(
    val id: String,
    val name: String,
)
