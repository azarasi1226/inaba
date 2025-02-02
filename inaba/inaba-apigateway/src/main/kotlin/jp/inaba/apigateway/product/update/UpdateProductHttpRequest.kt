package jp.inaba.apigateway.product.update

import jp.inaba.grpc.product.UpdateProductRequest

data class UpdateProductHttpRequest(
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Int,
) {
    fun toGrpcRequest(id: String): UpdateProductRequest {
        return UpdateProductRequest.newBuilder()
            .setId(id)
            .setName(name)
            .setDescription(description)
            .setImageUrl(imageUrl)
            .setPrice(price)
            .build()
    }
}