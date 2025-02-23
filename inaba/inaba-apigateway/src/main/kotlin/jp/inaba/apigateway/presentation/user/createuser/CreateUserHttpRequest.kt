package jp.inaba.apigateway.presentation.user.createuser

import jp.inaba.grpc.user.CreateUserRequest

data class CreateUserHttpRequest(
    val id: String,
    val subject: String,
) {
    fun toGrpcRequest(): CreateUserRequest {
        return CreateUserRequest.newBuilder()
            .setId(id)
            .setSubject(subject)
            .build()
    }
}
