package jp.inaba.service.presentation.product

import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.product.ProductId
import jp.inaba.grpc.product.FindProductByIdGrpc
import jp.inaba.grpc.product.FindProductByIdRequest
import jp.inaba.grpc.product.FindProductByIdResponse
import jp.inaba.message.product.query.FindProductByIdQuery
import jp.inaba.message.product.query.FindProductByIdResult
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.extensions.kotlin.query
import org.axonframework.queryhandling.QueryGateway

@GrpcService
class
FindProductByIdGrpcService(
    private val queryGateway: QueryGateway,
) : FindProductByIdGrpc.FindProductByIdImplBase() {
    override fun handle(
        request: FindProductByIdRequest,
        responseObserver: StreamObserver<FindProductByIdResponse>,
    ) {
        val query = FindProductByIdQuery(ProductId(request.id))

        val result = queryGateway.query<FindProductByIdResult, FindProductByIdQuery>(query).get()

        val response =
            FindProductByIdResponse.newBuilder()
                .setName(result.name)
                .setDescription(result.description)
                .apply { if (result.imageUrl != null) setImageUrl(result.imageUrl) }
                .setPrice(result.price)
                .setQuantity(result.quantity)
                .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}
