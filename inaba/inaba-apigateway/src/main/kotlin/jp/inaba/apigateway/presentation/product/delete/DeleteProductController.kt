package jp.inaba.apigateway.presentation.product.delete

import io.swagger.v3.oas.annotations.Operation
import jp.inaba.apigateway.presentation.product.ProductController
import jp.inaba.grpc.product.DeleteProductGrpc
import jp.inaba.grpc.product.DeleteProductRequest
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class DeleteProductController(
    @GrpcClient("global")
    private val grpcService: DeleteProductGrpc.DeleteProductBlockingStub
) : ProductController {
    @DeleteMapping("/api/products/{id}")
    @Operation(
        operationId = "deleteProduct"
    )
    fun handle(
        @PathVariable("id")
        id: String
    ) {
        val grpcRequest = DeleteProductRequest.newBuilder()
            .setId(id)
            .build()

        grpcService.handle(grpcRequest)
    }
}