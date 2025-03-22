package jp.inaba.service.presentation.product

import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.common.IdempotencyId
import jp.inaba.core.domain.product.IncreaseStockQuantity
import jp.inaba.core.domain.product.ProductId
import jp.inaba.grpc.stock.IncreaseStockGrpc
import jp.inaba.grpc.stock.IncreaseStockRequest
import jp.inaba.message.product.command.IncreaseStockCommand
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.commandhandling.gateway.CommandGateway

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
                increaseStockQuantity = IncreaseStockQuantity(request.increaseCount),
            )

        commandGateway.sendAndWait<Any>(command)

        responseObserver.onNext(Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }
}
