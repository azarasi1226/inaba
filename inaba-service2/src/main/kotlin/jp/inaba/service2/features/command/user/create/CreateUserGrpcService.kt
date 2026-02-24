package jp.inaba.service2.features.user.create

import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.user.UserId
import jp.inaba.grpc.user.CreateUserGrpc
import jp.inaba.grpc.user.CreateUserRequest
import jp.inaba.message.throwIfError
import jp.inaba.message.user.command.CreateUserCommand
import jp.inaba.message.user.command.createUser
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.messaging.commandhandling.gateway.CommandGateway

@GrpcService
class CreateUserGrpcService(
    private val commandGateway: CommandGateway,
) : CreateUserGrpc.CreateUserImplBase() {
    override fun handle(
        request: CreateUserRequest,
        responseObserver: StreamObserver<Empty>,
    ) {
        val command =
            CreateUserCommand(
                id = UserId(request.id),
                subject = request.subject,
            )

        commandGateway.createUser(command).throwIfError()

        responseObserver.onNext(Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }
}
