package jp.inaba.service2.feature.query.user.findmetadata

import io.grpc.stub.StreamObserver
import jp.inaba.grpc.user.FindUserMetadataBySubjectGrpc
import jp.inaba.grpc.user.FindUserMetadataBySubjectRequest
import jp.inaba.grpc.user.FindUserMetadataBySubjectResponse
import jp.inaba.message.getOrThrow
import jp.inaba.message.user.query.FindUserMetadataBySubjectQuery
import jp.inaba.message.user.query.findUserMetadataBySubject
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.messaging.queryhandling.gateway.QueryGateway

@GrpcService
class FindUserMetadataBySubjectGrpcService(
    private val queryGateway: QueryGateway,
) : FindUserMetadataBySubjectGrpc.FindUserMetadataBySubjectImplBase() {
    override fun handle(
        request: FindUserMetadataBySubjectRequest,
        responseObserver: StreamObserver<FindUserMetadataBySubjectResponse>,
    ) {
        val query = FindUserMetadataBySubjectQuery(subject = request.subject)
        val result = queryGateway.findUserMetadataBySubject(query).getOrThrow()

        val response = FindUserMetadataBySubjectResponse.newBuilder()
            .setUserId(result.userId)
            .setBasketId(result.basketId)
            .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}
