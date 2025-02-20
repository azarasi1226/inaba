package jp.inaba.apigateway.product.search

import jp.inaba.apigateway.common.Page
import jp.inaba.apigateway.common.Paging
import jp.inaba.grpc.product.SearchProductsResponse

data class SearchProductsHttpResponse(
    val page: Page<Summary>,
) {
    data class Summary(
        val id: String,
        val name: String,
        val imageUrl: String,
        val price: Int,
        val quantity: Int,
    )

    companion object {
        fun fromGrpcResponse(grpcResponse: SearchProductsResponse): SearchProductsHttpResponse {
            return SearchProductsHttpResponse(
                page =
                    Page(
                        items =
                            grpcResponse.itemsList.map {
                                Summary(
                                    id = it.id,
                                    name = it.name,
                                    imageUrl = it.imageUrl,
                                    price = it.price,
                                    quantity = it.quantity,
                                )
                            },
                        paging =
                            Paging(
                                totalCount = grpcResponse.paging.totalCount,
                                pageSize = grpcResponse.paging.pageSize,
                                pageNumber = grpcResponse.paging.pageNumber,
                            ),
                    ),
            )
        }
    }
}
