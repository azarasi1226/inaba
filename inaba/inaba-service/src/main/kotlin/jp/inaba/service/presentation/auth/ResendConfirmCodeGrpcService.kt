package jp.inaba.service.presentation.auth

import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import jp.inaba.grpc.auth.ResendConfirmCodeGrpc
import jp.inaba.grpc.auth.ResendConfirmCodeRequest
import jp.inaba.message.auth.command.ResendConfirmCodeCommand
import jp.inaba.message.auth.resendConfirmCode
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.commandhandling.gateway.CommandGateway

@GrpcService
class ResendConfirmCodeGrpcService(
    private val commandGateway: CommandGateway
) : ResendConfirmCodeGrpc.ResendConfirmCodeImplBase() {
    override fun handle(request: ResendConfirmCodeRequest, responseObserver: StreamObserver<Empty>) {
        val command = ResendConfirmCodeCommand(
            emailAddress = request.emailAddress
        )

        commandGateway.resendConfirmCode(command)

        responseObserver.onNext(Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }
}