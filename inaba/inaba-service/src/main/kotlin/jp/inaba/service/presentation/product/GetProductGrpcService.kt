package jp.inaba.service.presentation.product

import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.product.ProductId
import jp.inaba.grpc.product.GetProductGrpc
import jp.inaba.grpc.product.GetProductRequest
import jp.inaba.grpc.product.GetProductResponse
import jp.inaba.message.product.query.FindProductByIdQuery
import jp.inaba.message.product.query.FindProductByIdResult
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.extensions.kotlin.query
import org.axonframework.queryhandling.QueryGateway

@GrpcService
class GetProductGrpcService(
    private val queryGateway: QueryGateway,
) : GetProductGrpc.GetProductImplBase() {
    override fun handle(
        request: GetProductRequest,
        responseObserver: StreamObserver<GetProductResponse>,
    ) {
        val query =
            FindProductByIdQuery(
                id = ProductId(request.id),
            )

        val result = queryGateway.query<FindProductByIdResult, FindProductByIdQuery>(query).get()

        val response = GetProductResponse.newBuilder()
            .setName(result.name)
            .setStockId(result.stockId)
            .setDescription(result.description)
            .setImageUrl(result.imageUrl)
            .setPrice(result.price)
            .setQuantity(result.quantity)
            .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}
