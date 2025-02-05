package jp.inaba.apigateway.auth.confirmsignup

import jp.inaba.grpc.auth.ConfirmSignupRequest

data class ConfirmSignupHttpRequest(
    val emailAddress: String,
    val confirmCode: String,
) {
    fun toGrpcRequest(): ConfirmSignupRequest {
        return ConfirmSignupRequest.newBuilder()
            .setEmailAddress(emailAddress)
            .setConfirmCode(confirmCode)
            .build()
    }
}