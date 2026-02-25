package jp.inaba.message.brand.query

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.message.Error
import jp.inaba.message.QueryResult

data class FindBrandByIdQuery(
    val id: BrandId,
)

data class FindBrandByIdPayload(
    val id: String,
    val name: String,
)

class FindBrandByIdResult private constructor(
    override val success: Boolean,
    override val data: FindBrandByIdPayload?,
    override val error: Error?,
) : QueryResult<FindBrandByIdPayload> {
    companion object {
        fun success(payload: FindBrandByIdPayload): FindBrandByIdResult =
            FindBrandByIdResult(true, payload, null)

        fun brandNotFound(): FindBrandByIdResult =
            FindBrandByIdResult(false, null, Error("ブランドが存在しません"))
    }
}
