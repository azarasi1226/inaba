package jp.inaba.apigateway.user.createuser

import jp.inaba.apigateway.user.UserController
import jp.inaba.grpc.user.CreateUserGrpc
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CreateUserController(
    @GrpcClient("global")
    private val grpcService: CreateUserGrpc.CreateUserBlockingStub,
) : UserController {
    @PostMapping("/api/users")
    fun handle(
        @RequestBody
        request: CreateUserHttpRequest
    ) {
        val grpcRequest = request.toGrpcRequest()

        grpcService.handle(grpcRequest)
    }
}