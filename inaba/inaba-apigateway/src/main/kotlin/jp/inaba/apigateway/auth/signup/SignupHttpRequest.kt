package jp.inaba.apigateway.auth.signup

import jp.inaba.grpc.auth.SignupRequest

data class SignupHttpRequest(
    val emailAddress: String,
    val password: String,
) {
    fun toGrpcRequest(): SignupRequest {
        return SignupRequest.newBuilder()
            .setEmailAddress(emailAddress)
            .setPassword(password)
            .build()
    }
}
