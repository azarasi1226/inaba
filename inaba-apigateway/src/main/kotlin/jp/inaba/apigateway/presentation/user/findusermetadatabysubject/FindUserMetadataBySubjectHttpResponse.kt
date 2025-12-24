package jp.inaba.apigateway.presentation.user.findusermetadatabysubject

import jp.inaba.grpc.user.FindUserMetadataBySubjectResponse

data class FindUserMetadataBySubjectHttpResponse(
    val userId: String,
    val basketId: String,
) {
    companion object {
        fun fromGrpcResponse(response: FindUserMetadataBySubjectResponse): FindUserMetadataBySubjectHttpResponse =
            FindUserMetadataBySubjectHttpResponse(
                userId = response.userId,
                basketId = response.basketId,
            )
    }
}
