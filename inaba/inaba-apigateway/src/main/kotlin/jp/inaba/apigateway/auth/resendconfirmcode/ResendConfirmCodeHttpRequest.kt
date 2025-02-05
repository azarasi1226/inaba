package jp.inaba.apigateway.auth.resendconfirmcode

import jp.inaba.grpc.auth.ResendConfirmCodeRequest

data class ResendConfirmCodeHttpRequest(
    val emailAddress: String,
) {
    fun toGrpcRequest(): ResendConfirmCodeRequest {
        return ResendConfirmCodeRequest.newBuilder()
            .setEmailAddress(emailAddress)
            .build()
    }
}