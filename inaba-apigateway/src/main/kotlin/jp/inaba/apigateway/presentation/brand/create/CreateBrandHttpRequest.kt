package jp.inaba.apigateway.presentation.brand.create

import jp.inaba.grpc.brand.CreateBrandRequest

data class CreateBrandHttpRequest(
    val id: String,
    val name: String,
) {
    fun toGrpcRequest(): CreateBrandRequest =
        CreateBrandRequest
            .newBuilder()
            .setId(id)
            .setName(name)
            .build()
}
