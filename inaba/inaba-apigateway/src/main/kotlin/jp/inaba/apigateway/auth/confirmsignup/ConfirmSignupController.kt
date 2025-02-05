package jp.inaba.apigateway.auth.confirmsignup

import jp.inaba.apigateway.auth.AuthController
import jp.inaba.grpc.auth.ConfirmSignupGrpc
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ConfirmSignupController(
    @GrpcClient("global")
    private val grpcService: ConfirmSignupGrpc.ConfirmSignupBlockingStub,
) : AuthController {
    @PostMapping("/api/auth/confirm-signup")
    fun handle(
        @RequestBody
        request: ConfirmSignupHttpRequest
    ) {
        val grpcRequest = request.toGrpcRequest()

        grpcService.handle(grpcRequest)
    }
}