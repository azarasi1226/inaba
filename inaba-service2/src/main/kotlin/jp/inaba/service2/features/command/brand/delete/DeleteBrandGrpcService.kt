package jp.inaba.service2.features.command.brand.delete

import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.brand.BrandId
import jp.inaba.grpc.brand.DeleteBrandGrpc
import jp.inaba.grpc.brand.DeleteBrandRequest
import jp.inaba.message.brand.deleteBrand
import jp.inaba.message.brand.command.DeleteBrandCommand
import jp.inaba.message.throwIfError
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.messaging.commandhandling.gateway.CommandGateway

@GrpcService
class DeleteBrandGrpcService(
    private val commandGateway: CommandGateway,
) : DeleteBrandGrpc.DeleteBrandImplBase() {
    override fun handle(
        request: DeleteBrandRequest,
        responseObserver: StreamObserver<Empty>,
    ) {
        val command = DeleteBrandCommand(
            id = BrandId(request.id),
        )

        commandGateway.deleteBrand(command).throwIfError()

        responseObserver.onNext(Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }
}
