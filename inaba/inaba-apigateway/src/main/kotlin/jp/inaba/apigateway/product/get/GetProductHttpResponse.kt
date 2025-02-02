package jp.inaba.apigateway.product.get

import jp.inaba.grpc.product.GetProductResponse

data class GetProductHttpResponse(
    val stockId: String,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Int,
) {
    companion object {
        fun fromGrpcResponse(grpcResponse: GetProductResponse): GetProductHttpResponse {
            return GetProductHttpResponse(
                stockId = grpcResponse.stockId,
                name = grpcResponse.name,
                description = grpcResponse.description,
                imageUrl = grpcResponse.imageUrl,
                price = grpcResponse.price,
            )
        }
    }
}
