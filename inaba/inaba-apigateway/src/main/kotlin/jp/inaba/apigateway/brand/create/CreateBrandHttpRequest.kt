package jp.inaba.apigateway.brand.create

import jp.inaba.grpc.brand.CreateBrandRequest

data class CreateBrandHttpRequest(
    val id: String,
    val name: String,
) {
    fun toGrpcRequest(): CreateBrandRequest {
        return CreateBrandRequest.newBuilder()
            .setId(id)
            .setName(name)
            .build()
    }
}