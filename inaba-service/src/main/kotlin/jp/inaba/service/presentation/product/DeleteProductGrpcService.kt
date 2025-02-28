package jp.inaba.service.presentation.product

import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.product.ProductId
import jp.inaba.grpc.product.DeleteProductGrpc
import jp.inaba.grpc.product.DeleteProductRequest
import jp.inaba.message.product.command.DeleteProductCommand
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.commandhandling.gateway.CommandGateway

@GrpcService
class DeleteProductGrpcService(
    private val commandGateway: CommandGateway,
) : DeleteProductGrpc.DeleteProductImplBase() {
    override fun handle(
        request: DeleteProductRequest,
        responseObserver: StreamObserver<Empty>,
    ) {
        val command = DeleteProductCommand(ProductId(request.id))

        commandGateway.sendAndWait<Any>(command)

        responseObserver.onNext(Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }
}
