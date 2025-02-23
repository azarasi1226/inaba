package jp.inaba.apigateway.presentation.product.findbyid

import jp.inaba.apigateway.presentation.product.ProductController
import jp.inaba.grpc.product.FindProductByIdGrpc
import jp.inaba.grpc.product.FindProductByIdRequest
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class FindProductByIdController(
    @GrpcClient("global")
    private val grpcService: FindProductByIdGrpc.FindProductByIdBlockingStub,
) : ProductController {
    @GetMapping("/api/products/{id}")
    fun handle(
        @PathVariable("id")
        id: String,
    ): FindProductByIdHttpResponse {
        val grpcRequest =
            FindProductByIdRequest.newBuilder()
                .setId(id)
                .build()

        val grpcResponse = grpcService.handle(grpcRequest)

        return FindProductByIdHttpResponse.fromGrpcResponse(grpcResponse)
    }
}
