package jp.inaba.service.presentation.basket

import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.product.ProductId
import jp.inaba.grpc.basket.DeleteBasketItemGrpc
import jp.inaba.grpc.basket.DeleteBasketItemRequest
import jp.inaba.message.basket.command.DeleteBasketItemCommand
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.commandhandling.gateway.CommandGateway

@GrpcService
class DeleteBasketItemGrpcService(
    private val commandGateway: CommandGateway,
) : DeleteBasketItemGrpc.DeleteBasketItemImplBase() {
    override fun handle(request: DeleteBasketItemRequest, responseObserver: StreamObserver<Empty>) {
        val command =
            DeleteBasketItemCommand(
                id = BasketId(request.id),
                productId = ProductId(request.productId),
            )

        commandGateway.sendAndWait<Any>(command)

        responseObserver.onNext(Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }
}
