package jp.inaba.service.presentation.user

import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.user.UserId
import jp.inaba.grpc.user.CreateUserGrpc
import jp.inaba.grpc.user.CreateUserRequest
import jp.inaba.message.user.command.CreateUserCommand
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.commandhandling.gateway.CommandGateway

@GrpcService
class CreateUserGrpcService(
    private val commandGateway: CommandGateway
) : CreateUserGrpc.CreateUserImplBase() {
    override fun handle(request: CreateUserRequest, responseObserver: StreamObserver<Empty>) {
        val command = CreateUserCommand(
            id = UserId(request.id),
            subject = request.subject,
        )

        commandGateway.sendAndWait<Any>(command)

        responseObserver.onNext(Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }
}