package jp.inaba.service2.features.command.brand.create

import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.brand.BrandName
import jp.inaba.grpc.brand.CreateBrandGrpc
import jp.inaba.grpc.brand.CreateBrandRequest
import jp.inaba.message.brand.command.CreateBrandCommand
import jp.inaba.message.brand.command.createBrand
import jp.inaba.message.throwIfError
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.messaging.commandhandling.gateway.CommandGateway

@GrpcService
class CreateBrandGrpcService(
    private val commandGateway: CommandGateway,
) : CreateBrandGrpc.CreateBrandImplBase() {
    override fun handle(
        request: CreateBrandRequest,
        responseObserver: StreamObserver<Empty>,
    ) {
        val command =
            CreateBrandCommand(
                id = BrandId(request.id),
                name = BrandName(request.name),
            )

        commandGateway.createBrand(command).throwIfError()

        responseObserver.onNext(Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }
}
