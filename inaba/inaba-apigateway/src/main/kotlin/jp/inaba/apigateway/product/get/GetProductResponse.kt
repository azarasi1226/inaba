package jp.inaba.apigateway.product.get

import jp.inaba.grpc.product.GetProductResponse

data class GetProductResponse(
    val stockId: String,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Int,
) {
    companion object {
        fun fromGrpcResponse(response: GetProductResponse): jp.inaba.apigateway.product.get.GetProductResponse {
            return GetProductResponse(
                stockId = response.stockId,
                name = response.name,
                description = response.description,
                imageUrl = response.imageUrl,
                price = response.price,
            )
        }
    }
}
