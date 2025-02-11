package jp.inaba.apigateway.user.getusermetadata

import jp.inaba.apigateway.user.UserController
import jp.inaba.grpc.user.GetUserMetadataGrpc
import jp.inaba.grpc.user.GetUserMetadataRequest
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class GetUserMetadataController (
    @GrpcClient("global")
    private val grpcService: GetUserMetadataGrpc.GetUserMetadataBlockingStub,
) : UserController {
    @GetMapping("/api/users/metadatas/{subject}")
    fun handle(
        @PathVariable("subject")
        subject: String
    ): GetUserMetadataHttpResponse {
        val grpcRequest = GetUserMetadataRequest.newBuilder()
            .setSubject(subject)
            .build()

        val grpcResponse = grpcService.handle(grpcRequest)

        return GetUserMetadataHttpResponse.fromGrpcResponse(grpcResponse)
    }
}