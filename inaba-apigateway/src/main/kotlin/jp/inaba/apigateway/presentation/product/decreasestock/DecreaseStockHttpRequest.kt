package jp.inaba.apigateway.presentation.product.decreasestock

import jp.inaba.grpc.product.DecreaseStockRequest

data class DecreaseStockHttpRequest(
    val decreaseCount: Int,
    val idempotencyId: String,
) {
    fun toGrpcRequest(id: String): DecreaseStockRequest {
        return DecreaseStockRequest.newBuilder()
            .setId(id)
            .setDecreaseStockQuantity(decreaseCount)
            .setIdempotencyId(idempotencyId)
            .build()
    }
}
