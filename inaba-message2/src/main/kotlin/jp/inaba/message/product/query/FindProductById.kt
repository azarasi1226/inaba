package jp.inaba.message.product.query

import jp.inaba.core.domain.product.ProductId
import jp.inaba.message.Error
import jp.inaba.message.QueryResult2
import org.axonframework.messaging.queryhandling.gateway.QueryGateway

data class FindProductByIdQuery(
    val id: ProductId,
)

data class FindProductByIdData(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Int,
    val quantity: Int,
)

class FindProductByIdResult private constructor(
    override val success: Boolean,
    override val data: FindProductByIdData?,
    override val error: Error?,
) : QueryResult2<FindProductByIdData> {
    companion object {
        fun success(data: FindProductByIdData) = FindProductByIdResult(true, data, null)

        fun notFound() = FindProductByIdResult(false, null, Error("商品が存在しませんでした"))
    }
}

fun QueryGateway.findProductById(query: FindProductByIdQuery): FindProductByIdResult =
    this.query(query, FindProductByIdResult::class.java).join()
