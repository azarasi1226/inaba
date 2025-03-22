package jp.inaba.apigateway.presentation.product.findbyid

import jp.inaba.grpc.product.FindProductByIdResponse

data class FindProductByIdHttpResponse(
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Int,
    val quantity: Int,
) {
    companion object {
        fun fromGrpcResponse(grpcResponse: FindProductByIdResponse): FindProductByIdHttpResponse {
            return FindProductByIdHttpResponse(
                name = grpcResponse.name,
                description = grpcResponse.description,
                imageUrl = if (grpcResponse.hasImageUrl()) grpcResponse.imageUrl else null,
                price = grpcResponse.price,
                quantity = grpcResponse.quantity,
            )
        }
    }
}
