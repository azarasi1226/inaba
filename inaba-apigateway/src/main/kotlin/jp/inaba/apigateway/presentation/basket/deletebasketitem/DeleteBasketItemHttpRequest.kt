package jp.inaba.apigateway.presentation.basket.deletebasketitem

import jp.inaba.grpc.basket.DeleteBasketItemRequest

data class DeleteBasketItemHttpRequest(
    val id: String,
    val productId: String,
) {
    fun toGrpcRequest(): DeleteBasketItemRequest =
        DeleteBasketItemRequest
            .newBuilder()
            .setId(id)
            .setProductId(productId)
            .build()
}
