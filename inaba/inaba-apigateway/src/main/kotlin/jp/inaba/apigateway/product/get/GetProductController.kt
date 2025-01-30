package jp.inaba.apigateway.product.get

import jp.inaba.apigateway.product.ProductController
import jp.inaba.grpc.product.GetProductGrpc
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class GetProductController(
    @GrpcClient("global")
    private val grpcService: GetProductGrpc.GetProductBlockingStub
) : ProductController {
    @GetMapping("/api/product/{id}")
    fun handle(
        @PathVariable("id")
        id: String
    ) : GetProductResponse {
        val grpcRequest = jp.inaba.grpc.product.GetProductRequest.newBuilder()
            .setId(id)
            .build()

        val response = grpcService.handle(grpcRequest)

        return GetProductResponse.fromGrpcResponse(response)
    }
}