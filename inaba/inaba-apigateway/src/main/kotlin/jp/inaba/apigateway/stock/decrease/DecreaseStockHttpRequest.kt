package jp.inaba.apigateway.stock.decrease

import jp.inaba.grpc.stock.DecreaseStockRequest

data class DecreaseStockHttpRequest(
    val decreaseCount: Int,
    val idempotencyId: String,
) {
    fun toGrpcRequest(id: String): DecreaseStockRequest {
        return DecreaseStockRequest.newBuilder()
            .setId(id)
            .setDecreaseCount(decreaseCount)
            .setIdempotencyId(idempotencyId)
            .build()
    }
}