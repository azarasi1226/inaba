package jp.inaba.service.presentation.brand

import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.brand.BrandId
import jp.inaba.grpc.brand.DeleteBrandGrpc
import jp.inaba.grpc.brand.DeleteBrandRequest
import jp.inaba.message.brand.command.DeleteBrandCommand
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.commandhandling.gateway.CommandGateway

@GrpcService
class DeleteBrandGrpcService(
    private val commandGateway: CommandGateway,
) : DeleteBrandGrpc.DeleteBrandImplBase() {
    override fun handle(
        request: DeleteBrandRequest,
        responseObserver: StreamObserver<Empty>,
    ) {
        val command = DeleteBrandCommand(BrandId(request.id))

        commandGateway.sendAndWait<Any>(command)

        responseObserver.onNext(Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }
}
