package jp.inaba.apigateway.presentation.product.search

import jp.inaba.apigateway.presentation.common.PagingCondition
import jp.inaba.grpc.common.SortCondition
import jp.inaba.grpc.product.SearchProductsRequest

data class SearchProductsHttpRequest(
    val name: String,
    val pagingCondition: PagingCondition,
    val sortCondition: jp.inaba.apigateway.presentation.common.SortCondition,
) {
    fun toGrpcRequest(): SearchProductsRequest {
        return SearchProductsRequest.newBuilder()
            .setName(name)
            .setPagingCondition(
                jp.inaba.grpc.common.PagingCondition.newBuilder()
                    .setPageSize(pagingCondition.pageSize)
                    .setPageNumber(pagingCondition.pageNumber)
                    .build(),
            )
            .setSortCondition(
                SortCondition.newBuilder()
                    .setProperty(sortCondition.property)
                    .setDirection(sortCondition.direction.name)
                    .build(),
            )
            .build()
    }
}
