package jp.inaba.service.presentation.basket

import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.basket.BasketItemQuantity
import jp.inaba.core.domain.product.ProductId
import jp.inaba.grpc.basket.SetBasketItemGrpc
import jp.inaba.grpc.basket.SetBasketItemRequest
import jp.inaba.message.basket.command.SetBasketItemCommand
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.commandhandling.gateway.CommandGateway

@GrpcService
class SetBasketItemGrpcService(
    private val commandGateway: CommandGateway,
) : SetBasketItemGrpc.SetBasketItemImplBase() {
    override fun handle(
        request: SetBasketItemRequest,
        responseObserver: StreamObserver<Empty>,
    ) {
        val command =
            SetBasketItemCommand(
                id = BasketId(request.basketId),
                productId = ProductId(request.productId),
                basketItemQuantity = BasketItemQuantity(request.itemQuantity),
            )

        commandGateway.sendAndWait<Any>(command)

        responseObserver.onNext(Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }
}
