package jp.inaba.apigateway.product.create

import jp.inaba.grpc.product.CreateProductRequest

data class CreateProductRequest(
    var name: String,
    var description: String,
    var imageUrl: String,
    var price: Int,
) {
    fun toGrpcRequest(): CreateProductRequest{
        return CreateProductRequest.newBuilder()
            .setName(name)
            .setDescription(description)
            .setImageUrl(imageUrl)
            .setPrice(price)
            .build()
    }
}

