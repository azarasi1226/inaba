package jp.inaba.apigateway.user.findusermetadatabysubject

import jp.inaba.apigateway.user.UserController
import jp.inaba.grpc.user.FindUserMetadataBySubjectGrpc
import jp.inaba.grpc.user.FindUserMetadataBySubjectRequest
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class FindUserMetadataBySubjectController(
    @GrpcClient("global")
    private val grpcService: FindUserMetadataBySubjectGrpc.FindUserMetadataBySubjectBlockingStub,
) : UserController {
    @GetMapping("/api/users/metadatas/{subject}")
    fun handle(
        @PathVariable("subject")
        subject: String,
    ): FindUserMetadataBySubjectHttpResponse {
        val grpcRequest =
            FindUserMetadataBySubjectRequest.newBuilder()
                .setSubject(subject)
                .build()

        val grpcResponse = grpcService.handle(grpcRequest)

        return FindUserMetadataBySubjectHttpResponse.fromGrpcResponse(grpcResponse)
    }
}
