package jp.inaba.service.presentation.user

import io.grpc.stub.StreamObserver
import jp.inaba.grpc.user.FindUserMetadataBySubjectGrpc
import jp.inaba.grpc.user.FindUserMetadataBySubjectRequest
import jp.inaba.grpc.user.FindUserMetadataBySubjectResponse
import jp.inaba.message.user.query.FindUserMetadataBySubjectQuery
import jp.inaba.message.user.query.FindUserMetadataBySubjectResult
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.extensions.kotlin.query
import org.axonframework.queryhandling.QueryGateway

@GrpcService
class GetUserMetadataGrpcService(
    private val queryGateway: QueryGateway,
) : FindUserMetadataBySubjectGrpc.FindUserMetadataBySubjectImplBase() {
    override fun handle(
        request: FindUserMetadataBySubjectRequest,
        responseObserver: StreamObserver<FindUserMetadataBySubjectResponse>,
    ) {
        val query =
            FindUserMetadataBySubjectQuery(
                subject = request.subject,
            )

        val result = queryGateway.query<FindUserMetadataBySubjectResult, FindUserMetadataBySubjectQuery>(query).get()

        val response =
            FindUserMetadataBySubjectResponse.newBuilder()
                .setUserId(result.userId)
                .setBasketId(result.basketId)
                .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}
