package jp.inaba.apigateway.product.update

import jp.inaba.apigateway.product.ProductController
import jp.inaba.grpc.product.UpdateProductGrpc
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UpdateProductController(
    @GrpcClient("global")
    private val grpcService: UpdateProductGrpc.UpdateProductBlockingStub,
) : ProductController {
    @PutMapping("/api/products/{id}")
    fun handle(
        @PathVariable("id")
        id: String,
        @RequestBody
        request: UpdateProductHttpRequest
    ) {
        val grpcRequest = request.toGrpcRequest(id)

        grpcService.handle(grpcRequest)
    }
}