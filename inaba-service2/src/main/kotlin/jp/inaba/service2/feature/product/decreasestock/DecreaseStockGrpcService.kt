package jp.inaba.service2.feature.command.product.decreasestock

import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.common.IdempotencyId
import jp.inaba.core.domain.product.DecreaseStockQuantity
import jp.inaba.core.domain.product.ProductId
import jp.inaba.grpc.product.DecreaseStockGrpc
import jp.inaba.grpc.product.DecreaseStockRequest
import jp.inaba.message.product.command.DecreaseStockCommand
import jp.inaba.message.product.command.decreaseStock
import jp.inaba.message.throwIfError
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.messaging.commandhandling.gateway.CommandGateway

@GrpcService
class DecreaseStockGrpcService(
    private val commandGateway: CommandGateway,
) : DecreaseStockGrpc.DecreaseStockImplBase() {
    override fun handle(
        request: DecreaseStockRequest,
        responseObserver: StreamObserver<Empty>,
    ) {
        val command =
            DecreaseStockCommand(
                id = ProductId(request.id),
                idempotencyId = IdempotencyId(request.idempotencyId),
                decreaseStockQuantity = DecreaseStockQuantity(request.decreaseStockQuantity),
            )

        commandGateway.decreaseStock(command).throwIfError()

        responseObserver.onNext(Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }
}
