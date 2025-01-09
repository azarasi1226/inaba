package jp.inaba.service.presentation.auth

import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import jp.inaba.grpc.auth.ConfirmSignupGrpc
import jp.inaba.grpc.auth.ConfirmSignupRequest
import jp.inaba.message.auth.command.ConfirmSignupCommand
import jp.inaba.message.auth.confirmSignup
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.commandhandling.gateway.CommandGateway

@GrpcService
class ConfirmSignupGrpcService(
    private val commandGateway: CommandGateway
) : ConfirmSignupGrpc.ConfirmSignupImplBase() {
    override fun handle(request: ConfirmSignupRequest, responseObserver: StreamObserver<Empty>) {
        val command = ConfirmSignupCommand(
            emailAddress = request.emailAddress,
            confirmCode = request.confirmCode
        )

        commandGateway.confirmSignup(command)

        responseObserver.onNext(Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }
}