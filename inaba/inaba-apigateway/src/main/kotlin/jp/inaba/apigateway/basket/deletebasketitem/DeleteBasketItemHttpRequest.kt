package jp.inaba.apigateway.basket.deletebasketitem

import jp.inaba.grpc.basket.DeleteBasketItemRequest

data class DeleteBasketItemHttpRequest(
    val id: String,
    val productId: String,
) {
    fun toGrpcRequest(): DeleteBasketItemRequest {
        return DeleteBasketItemRequest.newBuilder()
            .setId(id)
            .setProductId(productId)
            .build()
    }
}
