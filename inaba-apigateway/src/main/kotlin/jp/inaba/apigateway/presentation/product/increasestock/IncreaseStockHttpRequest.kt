package jp.inaba.apigateway.presentation.product.increasestock

import jp.inaba.grpc.stock.IncreaseStockRequest

data class IncreaseStockHttpRequest(
    val increaseCount: Int,
    val idempotencyId: String,
) {
    fun toGrpcRequest(id: String): IncreaseStockRequest {
        return IncreaseStockRequest.newBuilder()
            .setId(id)
            .setIncreaseCount(increaseCount)
            .setIdempotencyId(idempotencyId)
            .build()
    }
}
