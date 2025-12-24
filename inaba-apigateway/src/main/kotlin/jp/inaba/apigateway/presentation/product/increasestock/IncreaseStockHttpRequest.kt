package jp.inaba.apigateway.presentation.product.increasestock

import jp.inaba.grpc.product.IncreaseStockRequest

data class IncreaseStockHttpRequest(
    val increaseCount: Int,
    val idempotencyId: String,
) {
    fun toGrpcRequest(id: String): IncreaseStockRequest =
        IncreaseStockRequest
            .newBuilder()
            .setId(id)
            .setIncreaseStockQuantity(increaseCount)
            .setIdempotencyId(idempotencyId)
            .build()
}
