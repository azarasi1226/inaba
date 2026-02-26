package jp.inaba.service2.feature.command.product.delete

import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.product.ProductId
import jp.inaba.grpc.product.DeleteProductGrpc
import jp.inaba.grpc.product.DeleteProductRequest
import jp.inaba.message.product.command.DeleteProductCommand
import jp.inaba.message.product.command.deleteProduct
import jp.inaba.message.throwIfError
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.messaging.commandhandling.gateway.CommandGateway

@GrpcService
class DeleteProductGrpcService(
    private val commandGateway: CommandGateway,
) : DeleteProductGrpc.DeleteProductImplBase() {
    override fun handle(
        request: DeleteProductRequest,
        responseObserver: StreamObserver<Empty>,
    ) {
        val command =
            DeleteProductCommand(
                id = ProductId(request.id),
            )

        commandGateway.deleteProduct(command).throwIfError()

        responseObserver.onNext(Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }
}
