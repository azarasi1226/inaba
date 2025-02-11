package jp.inaba.apigateway.user.getusermetadata

import jp.inaba.grpc.user.GetUserMetadataResponse

data class GetUserMetadataHttpResponse(
    val userId: String,
    val basketId: String
) {
    companion object {
        fun fromGrpcResponse(response: GetUserMetadataResponse):GetUserMetadataHttpResponse {
            return GetUserMetadataHttpResponse(
                userId = response.userId,
                basketId = response.basketId
            )
        }
    }
}