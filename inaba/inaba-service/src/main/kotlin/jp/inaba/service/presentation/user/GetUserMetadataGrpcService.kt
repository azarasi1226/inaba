package jp.inaba.service.presentation.user

import io.grpc.stub.StreamObserver
import jp.inaba.grpc.user.GetUserMetadataGrpc
import jp.inaba.grpc.user.GetUserMetadataRequest
import jp.inaba.grpc.user.GetUserMetadataResponse
import jp.inaba.message.user.query.FindUserMetadataBySubjectQuery
import jp.inaba.message.user.query.FindUserMetadataBySubjectResult
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.extensions.kotlin.query
import org.axonframework.queryhandling.QueryGateway

@GrpcService
class GetUserMetadataGrpcService(
    private val queryGateway: QueryGateway
) : GetUserMetadataGrpc.GetUserMetadataImplBase() {
    override fun handle(request: GetUserMetadataRequest, responseObserver: StreamObserver<GetUserMetadataResponse>) {
        val query = FindUserMetadataBySubjectQuery(
            subject = request.subject
        )

        val result = queryGateway.query<FindUserMetadataBySubjectResult, FindUserMetadataBySubjectQuery>(query).get()

        val response =
            GetUserMetadataResponse.newBuilder()
                .setUserId(result.userId)
                .setBasketId(result.basketId)
                .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}