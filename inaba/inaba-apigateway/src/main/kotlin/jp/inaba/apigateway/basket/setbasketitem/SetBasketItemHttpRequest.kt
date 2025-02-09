package jp.inaba.apigateway.basket.setbasketitem

import jp.inaba.grpc.basket.SetBasketItemRequest

data class SetBasketItemHttpRequest(
    val productId: String,
    val quantity: Int
) {
    fun toGrpcRequest(id: String) : SetBasketItemRequest {
        return SetBasketItemRequest.newBuilder()
            .setBasketId(id)
            .setProductId(productId)
            .setItemQuantity(quantity)
            .build()
    }
}
