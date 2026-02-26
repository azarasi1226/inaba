package jp.inaba.message.brand.query

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.message.Error
import jp.inaba.message.QueryResult2
import org.axonframework.messaging.queryhandling.gateway.QueryGateway

data class FindBrandByIdQuery(
    val id: BrandId,
)

data class FindBrandByIdData(
    val id: String,
    val name: String,
)

class FindBrandByIdResult private constructor(
    override val success: Boolean,
    override val data: FindBrandByIdData?,
    override val error: Error?,
): QueryResult2<FindBrandByIdData> {
    companion object {
        fun success(payload: FindBrandByIdData) = FindBrandByIdResult(true, payload, null)
        fun notFound() = FindBrandByIdResult(false, null, Error("ブランドが存在しませんでした"))
    }
}

fun QueryGateway.findBrandById(query: FindBrandByIdQuery): FindBrandByIdResult =
    this.query(query, FindBrandByIdResult::class.java).join()