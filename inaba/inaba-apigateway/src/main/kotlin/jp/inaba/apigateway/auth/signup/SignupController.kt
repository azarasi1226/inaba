package jp.inaba.apigateway.auth.signup

import jp.inaba.apigateway.auth.AuthController
import jp.inaba.grpc.auth.SignupGrpc
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SignupController(
    @GrpcClient("global")
    private val grpcService: SignupGrpc.SignupBlockingStub
) : AuthController {
    @PostMapping("/api/auth/signup")
    fun handle(
        @RequestBody
        request: SignupHttpRequest
    ) {
        val grpcRequest = request.toGrpcRequest()

        grpcService.handle(grpcRequest)
    }
}