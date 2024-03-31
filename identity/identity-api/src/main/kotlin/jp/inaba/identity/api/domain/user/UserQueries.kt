package jp.inaba.identity.api.domain.user

object UserQueries {
    data class FindByIdQuery(val userId: UserId)

    data class FindByIdResult(
        val id: String,
        val name: String
    )
}