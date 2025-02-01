package jp.inaba.apigateway.product.create

import jp.inaba.apigateway.product.ProductController
import jp.inaba.grpc.product.CreateProductGrpc
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CreateProductController(
    @GrpcClient("global")
    private val grpcService: CreateProductGrpc.CreateProductBlockingStub,
) : ProductController {
    @PostMapping("/api/product")
    fun handle(
        @RequestBody
        request: CreateProductRequest,
    ) {
        val grpcRequest = request.toGrpcRequest()
        grpcService.handle(grpcRequest)
    }
}
