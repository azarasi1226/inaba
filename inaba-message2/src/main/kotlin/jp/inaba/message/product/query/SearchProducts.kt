package jp.inaba.message.product.query

import jp.inaba.core.domain.common.Page
import jp.inaba.core.domain.common.PagingCondition
import jp.inaba.core.domain.product.SearchProductSortCondition
import jp.inaba.message.Error
import jp.inaba.message.QueryResult2
import org.axonframework.messaging.queryhandling.gateway.QueryGateway

data class SearchProductsQuery(
    val likeProductName: String,
    val pagingCondition: PagingCondition,
    val sortCondition: SearchProductSortCondition,
)

data class SearchProductsSummary(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val price: Int,
    val quantity: Int,
)

class SearchProductsResult private constructor(
    override val success: Boolean,
    override val data: Page<SearchProductsSummary>?,
    override val error: Error?,
) : QueryResult2<Page<SearchProductsSummary>> {
    companion object {
        fun success(data: Page<SearchProductsSummary>) = SearchProductsResult(true, data, null)
    }
}

fun QueryGateway.searchProducts(query: SearchProductsQuery): SearchProductsResult =
    this.query(query, SearchProductsResult::class.java).join()
