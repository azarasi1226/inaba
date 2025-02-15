package jp.inaba.apigateway.product.create

import jp.inaba.grpc.product.CreateProductRequest

data class CreateProductHttpRequest(
    val id: String,
    val brandId: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Int,
) {
    fun toGrpcRequest(): CreateProductRequest {
        return CreateProductRequest.newBuilder()
            .setId(id)
            .setBrandId(brandId)
            .setName(name)
            .setDescription(description)
            .setImageUrl(imageUrl)
            .setPrice(price)
            .build()
    }
}
