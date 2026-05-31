package jp.inaba.service2.feature.query.product.findbyid

import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.product.ProductId
import jp.inaba.grpc.product.FindProductByIdGrpc
import jp.inaba.grpc.product.FindProductByIdRequest
import jp.inaba.grpc.product.FindProductByIdResponse
import jp.inaba.message.getOrThrow
import jp.inaba.message.product.query.FindProductByIdQuery
import jp.inaba.message.product.query.findProductById
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.messaging.queryhandling.gateway.QueryGateway

@GrpcService
class FindProductByIdGrpcService(
    private val queryGateway: QueryGateway,
) : FindProductByIdGrpc.FindProductByIdImplBase() {
    override fun handle(
        request: FindProductByIdRequest,
        responseObserver: StreamObserver<FindProductByIdResponse>,
    ) {
        val query = FindProductByIdQuery(ProductId(request.id))
        val result = queryGateway.findProductById(query).getOrThrow()

        val builder = FindProductByIdResponse.newBuilder()
            .setName(result.name)
            .setDescription(result.description)
            .setPrice(result.price)
            .setQuantity(result.quantity)

        if (result.imageUrl != null) {
            builder.setImageUrl(result.imageUrl)
        }

        responseObserver.onNext(builder.build())
        responseObserver.onCompleted()
    }
}
