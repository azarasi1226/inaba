package jp.inaba.service2.feature.command.product.increasestock

import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.common.IdempotencyId
import jp.inaba.core.domain.product.IncreaseStockQuantity
import jp.inaba.core.domain.product.ProductId
import jp.inaba.grpc.product.IncreaseStockGrpc
import jp.inaba.grpc.product.IncreaseStockRequest
import jp.inaba.message.product.command.IncreaseStockCommand
import jp.inaba.message.product.command.increaseStock
import jp.inaba.message.throwIfError
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.messaging.commandhandling.gateway.CommandGateway

@GrpcService
class IncreaseStockGrpcService(
    private val commandGateway: CommandGateway,
) : IncreaseStockGrpc.IncreaseStockImplBase() {
    override fun handle(
        request: IncreaseStockRequest,
        responseObserver: StreamObserver<Empty>,
    ) {
        val command =
            IncreaseStockCommand(
                id = ProductId(request.id),
                idempotencyId = IdempotencyId(request.idempotencyId),
                increaseStockQuantity = IncreaseStockQuantity(request.increaseStockQuantity),
            )

        commandGateway.increaseStock(command).throwIfError()

        responseObserver.onNext(Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }
}
