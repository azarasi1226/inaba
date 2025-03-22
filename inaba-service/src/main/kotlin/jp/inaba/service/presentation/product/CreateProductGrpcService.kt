package jp.inaba.service.presentation.product

import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.product.ProductDescription
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.product.ProductImageURL
import jp.inaba.core.domain.product.ProductName
import jp.inaba.core.domain.product.ProductPrice
import jp.inaba.core.domain.product.StockQuantity
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
                id = ProductId(request.id),
                brandId = BrandId(request.brandId),
                name = ProductName(request.name),
                description = ProductDescription(request.description),
                imageUrl = if (request.hasImageUrl()) ProductImageURL(request.imageUrl) else null,
                price = ProductPrice(request.price),
                quantity = StockQuantity(request.quantity)
            )

        commandGateway.sendAndWait<Any>(command)

        responseObserver.onNext(Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }
}
