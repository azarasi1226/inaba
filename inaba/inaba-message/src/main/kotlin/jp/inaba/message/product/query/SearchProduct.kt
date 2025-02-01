package jp.inaba.message.product.query

import jp.inaba.core.domain.common.Page
import jp.inaba.core.domain.common.PagingCondition
import jp.inaba.core.domain.common.SortCondition
import jp.inaba.core.domain.product.ProductSortProperty

data class SearchProductQuery(
    val likeProductName: String,
    val pagingCondition: PagingCondition,
    val sortCondition: SortCondition<ProductSortProperty>
)

data class SearchProductResult(
    val page: Page<Summary>
) {
    data class Summary(
        val id: String,
        val name: String,
        val imageUrl: String?,
        val price: Int,
        val quantity: Int,
    )
}


