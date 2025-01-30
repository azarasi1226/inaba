package jp.inaba.apigateway.product.search

import jp.inaba.apigateway.common.Page
import jp.inaba.apigateway.common.Paging

data class SearchProductResponse(
    val page : Page<Summary>
) {
    data class Summary(
        val name: String,
        val imageUrl: String,
        val quantity: Int,
    )
}