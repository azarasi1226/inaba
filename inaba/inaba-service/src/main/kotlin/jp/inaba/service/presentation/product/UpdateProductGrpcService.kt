package jp.inaba.service.presentation.product

import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.product.ProductDescription
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.product.ProductImageURL
import jp.inaba.core.domain.product.ProductName
import jp.inaba.core.domain.product.ProductPrice
import jp.inaba.grpc.product.UpdateProductGrpc
import jp.inaba.grpc.product.UpdateProductRequest
import jp.inaba.message.product.command.UpdateProductCommand
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.commandhandling.gateway.CommandGateway

@GrpcService
class UpdateProductGrpcService(
    private val commandGateway: CommandGateway,
) : UpdateProductGrpc.UpdateProductImplBase() {
    override fun handle(
        request: UpdateProductRequest,
        responseObserver: StreamObserver<Empty>,
    ) {
        val command =
            UpdateProductCommand(
                id = ProductId(request.id),
                name = ProductName(request.name),
                description = ProductDescription(request.description),
                imageUrl = if (request.hasImageUrl()) ProductImageURL(request.imageUrl) else null,
                price = ProductPrice(request.price),
            )

        commandGateway.sendAndWait<Any>(command)

        responseObserver.onNext(Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }
}
