package jp.inaba.service.presentation.basket

import com.github.michaelbull.result.mapBoth
import com.google.protobuf.Empty
import io.grpc.Status
import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.basket.BasketItemQuantity
import jp.inaba.core.domain.basket.SetBasketItemError
import jp.inaba.core.domain.product.ProductId
import jp.inaba.grpc.basket.SetBasketItemGrpc
import jp.inaba.grpc.basket.SetBasketItemRequest
import jp.inaba.message.basket.command.SetBasketItemCommand
import jp.inaba.message.basket.setBasketItem
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.commandhandling.gateway.CommandGateway

@GrpcService
class SetBasketItemGrpcService(
    private val commandGateway: CommandGateway
) : SetBasketItemGrpc.SetBasketItemImplBase() {
    override fun handle(request: SetBasketItemRequest, responseObserver: StreamObserver<Empty>) {
        val command = SetBasketItemCommand(
            id = BasketId(request.basketId),
            productId = ProductId(request.productId),
            basketItemQuantity = BasketItemQuantity(request.itemQuantity)
        )

        commandGateway.setBasketItem(command)
            .mapBoth(
                success = { success(responseObserver) },
                failure = { failure(it, responseObserver) }
            )
    }

    private fun success(responseObserver: StreamObserver<Empty>) {
        responseObserver.onNext(Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }

    private fun failure(error: SetBasketItemError, responseObserver: StreamObserver<Empty>) {
        val status = when(error) {
            SetBasketItemError.BASKET_DELETED -> Status.NOT_FOUND
            SetBasketItemError.PRODUCT_NOT_FOUND -> Status.NOT_FOUND
            SetBasketItemError.PRODUCT_MAX_KIND_OVER -> Status.OUT_OF_RANGE
        }
        responseObserver.onError(status.asException())
    }
 }