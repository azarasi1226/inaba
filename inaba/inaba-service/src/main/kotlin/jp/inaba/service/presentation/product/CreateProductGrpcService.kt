package jp.inaba.service.presentation.product

import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.product.ProductDescription
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.product.ProductImageURL
import jp.inaba.core.domain.product.ProductName
import jp.inaba.core.domain.product.ProductPrice
import jp.inaba.grpc.product.CreateProductGrpc
import jp.inaba.grpc.product.CreateProductRequest
import jp.inaba.message.product.command.CreateProductCommand
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.commandhandling.gateway.CommandGateway

@GrpcService
class CreateProductGrpcService(
    private val commandGateway: CommandGateway,
) : CreateProductGrpc.CreateProductImplBase() {
    override fun handle(
        request: CreateProductRequest,
        responseObserver: StreamObserver<Empty>,
    ) {
        val command =
            CreateProductCommand(
                id = ProductId(),
                name = ProductName(request.name),
                description = ProductDescription(request.description),
                imageUrl = ProductImageURL(request.imageUrl),
                price = ProductPrice(request.price),
            )

       commandGateway.sendAndWait<Any>(command)

        responseObserver.onNext(Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }
}
