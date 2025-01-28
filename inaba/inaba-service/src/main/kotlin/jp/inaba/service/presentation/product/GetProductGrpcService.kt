package jp.inaba.service.presentation.product

import com.github.michaelbull.result.mapBoth
import io.grpc.Status
import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.product.FindProductByIdError
import jp.inaba.core.domain.product.ProductId
import jp.inaba.grpc.product.GetProductGrpc
import jp.inaba.grpc.product.GetProductRequest
import jp.inaba.grpc.product.GetProductResponse
import jp.inaba.message.product.findProductById
import jp.inaba.message.product.query.FindProductByIdQuery
import jp.inaba.message.product.query.FindProductByIdResult
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.queryhandling.QueryGateway

@GrpcService
class GetProductGrpcService(
    private val queryGateway: QueryGateway,
) : GetProductGrpc.GetProductImplBase() {
    override fun handle(
        request: GetProductRequest,
        responseObserver: StreamObserver<GetProductResponse>,
    ) {
        val command =
            FindProductByIdQuery(
                id = ProductId(request.id),
            )

        queryGateway.findProductById(command)
            .mapBoth(
                success = { success(it, responseObserver) },
                failure = { failure(it, responseObserver) },
            )
    }

    private fun success(
        result: FindProductByIdResult,
        responseObserver: StreamObserver<GetProductResponse>,
    ) {
        val response =
            GetProductResponse.newBuilder()
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

    private fun failure(
        error: FindProductByIdError,
        responseObserver: StreamObserver<GetProductResponse>,
    ) {
        val status =
            when (error) {
                FindProductByIdError.PRODUCT_NOT_FOUND -> Status.NOT_FOUND
            }
        responseObserver.onError(status.asException())
    }
}
