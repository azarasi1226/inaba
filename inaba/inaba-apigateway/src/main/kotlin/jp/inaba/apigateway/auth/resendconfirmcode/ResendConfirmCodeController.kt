package jp.inaba.apigateway.auth.resendconfirmcode

import jp.inaba.apigateway.auth.AuthController
import jp.inaba.grpc.auth.ResendConfirmCodeGrpc
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ResendConfirmCodeController(
    @GrpcClient("global")
    private val grpcService: ResendConfirmCodeGrpc.ResendConfirmCodeBlockingStub,
) : AuthController {
    @PostMapping("/api/auth/resend-confirm-code")
    fun handle(
        @RequestBody
        request: ResendConfirmCodeHttpRequest,
    ) {
        val grpcRequest = request.toGrpcRequest()

        grpcService.handle(grpcRequest)
    }
}
