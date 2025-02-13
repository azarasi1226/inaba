package jp.inaba.apigateway.product.findbyid

import jp.inaba.grpc.product.FindProductByIdResponse

data class FindProductByIdHttpResponse(
    val stockId: String,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Int,
    val quantity: Int,
) {
    companion object {
        fun fromGrpcResponse(grpcResponse: FindProductByIdResponse): FindProductByIdHttpResponse {
            return FindProductByIdHttpResponse(
                stockId = grpcResponse.stockId,
                name = grpcResponse.name,
                description = grpcResponse.description,
                imageUrl = grpcResponse.imageUrl,
                price = grpcResponse.price,
                quantity = grpcResponse.quantity,
            )
        }
    }
}
