package jp.inaba.message.user.query

import jp.inaba.core.domain.user.UserId
import jp.inaba.message.Error
import jp.inaba.message.QueryResult

class FindUserByIdResult private constructor(
    override val success: Boolean,
    override val data: FindUserByIdPayload?,
    override val error: Error?
) : QueryResult<FindUserByIdPayload> {
    companion object {
        fun success(payload: FindUserByIdPayload): FindUserByIdResult = FindUserByIdResult(true, payload, null)
        fun userNotFound(): FindUserByIdResult = FindUserByIdResult(false, null, Error("存在しないユーザーです"))
    }
}


data class FindUserByIdQuery(
    val userId: UserId,
)

data class FindUserByIdPayload(
    val id: String,
    val name: String,
)
