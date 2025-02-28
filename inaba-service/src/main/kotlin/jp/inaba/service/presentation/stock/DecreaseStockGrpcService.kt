package jp.inaba.service.presentation.stock

import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.common.IdempotencyId
import jp.inaba.core.domain.stock.DecreaseCount
import jp.inaba.core.domain.stock.StockId
import jp.inaba.grpc.stock.DecreaseStockGrpc
import jp.inaba.grpc.stock.DecreaseStockRequest
import jp.inaba.message.stock.command.DecreaseStockCommand
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.commandhandling.gateway.CommandGateway

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
                id = StockId(request.id),
                decreaseCount = DecreaseCount(request.decreaseCount),
                idempotencyId = IdempotencyId(request.idempotencyId),
            )

        commandGateway.sendAndWait<Any>(command)

        responseObserver.onNext(Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }
}
