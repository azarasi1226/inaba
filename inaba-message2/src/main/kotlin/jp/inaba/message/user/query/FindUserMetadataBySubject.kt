package jp.inaba.message.user.query

import jp.inaba.message.Error
import jp.inaba.message.QueryResult2
import org.axonframework.messaging.queryhandling.gateway.QueryGateway

data class FindUserMetadataBySubjectQuery(
    val subject: String,
)

data class FindUserMetadataBySubjectData(
    val userId: String,
    val basketId: String,
)

class FindUserMetadataBySubjectResult private constructor(
    override val success: Boolean,
    override val data: FindUserMetadataBySubjectData?,
    override val error: Error?,
) : QueryResult2<FindUserMetadataBySubjectData> {
    companion object {
        fun success(data: FindUserMetadataBySubjectData) = FindUserMetadataBySubjectResult(true, data, null)

        fun notFound() = FindUserMetadataBySubjectResult(false, null, Error("ユーザーメタデータが存在しませんでした"))
    }
}

fun QueryGateway.findUserMetadataBySubject(query: FindUserMetadataBySubjectQuery): FindUserMetadataBySubjectResult =
    this.query(query, FindUserMetadataBySubjectResult::class.java).join()
