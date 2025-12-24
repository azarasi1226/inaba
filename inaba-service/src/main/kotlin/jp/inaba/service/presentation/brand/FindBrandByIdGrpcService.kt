package jp.inaba.service.presentation.brand

import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.brand.BrandId
import jp.inaba.grpc.brand.FindBrandByIdGrpc
import jp.inaba.grpc.brand.FindBrandByIdRequest
import jp.inaba.grpc.brand.FindBrandByIdResponse
import jp.inaba.message.brand.query.FindBrandByIdQuery
import jp.inaba.message.brand.query.FindBrandByIdResult
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.extensions.kotlin.query
import org.axonframework.queryhandling.QueryGateway

@GrpcService
class FindBrandByIdGrpcService(
    private val queryGateway: QueryGateway,
) : FindBrandByIdGrpc.FindBrandByIdImplBase() {
    override fun handle(
        request: FindBrandByIdRequest,
        responseObserver: StreamObserver<FindBrandByIdResponse>,
    ) {
        val query = FindBrandByIdQuery(BrandId(request.id))

        val result = queryGateway.query<FindBrandByIdResult, FindBrandByIdQuery>(query).get()

        val response =
            FindBrandByIdResponse
                .newBuilder()
                .setName(result.name)
                .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}
