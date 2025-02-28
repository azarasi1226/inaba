package jp.inaba.service.presentation.stock

import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.common.IdempotencyId
import jp.inaba.core.domain.stock.IncreaseCount
import jp.inaba.core.domain.stock.StockId
import jp.inaba.grpc.stock.IncreaseStockGrpc
import jp.inaba.grpc.stock.IncreaseStockRequest
import jp.inaba.message.stock.command.IncreaseStockCommand
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
                id = StockId(request.id),
                increaseCount = IncreaseCount(request.increaseCount),
                idempotencyId = IdempotencyId(request.idempotencyId),
            )

        commandGateway.sendAndWait<Any>(command)

        responseObserver.onNext(Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }
}
