package jp.inaba.apigateway.basket.getbasket

import jp.inaba.apigateway.common.Page
import jp.inaba.apigateway.common.Paging
import jp.inaba.grpc.basket.GetBasketResponse

data class GetBasketHttpResponse(
    val page: Page<Summary>,
) {
    data class Summary(
        val productId: String,
        val productName: String,
        val productPrice: Int,
        val productImageUrl: String,
        val productQuantity: Int,
    )

    companion object {
        fun formGrpcResponse(grpcResponse: GetBasketResponse): GetBasketHttpResponse {
            return GetBasketHttpResponse(
                page =
                    Page(
                        items =
                            grpcResponse.basketItemsList.map {
                                Summary(
                                    productId = it.productId,
                                    productName = it.productName,
                                    productPrice = it.productPrice,
                                    productImageUrl = it.productImageUrl,
                                    productQuantity = it.productQuantity,
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
