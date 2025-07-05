package jp.inaba.apigateway.presentation.product.increasestock

import io.swagger.v3.oas.annotations.Operation
import jp.inaba.apigateway.presentation.product.ProductController
import jp.inaba.grpc.product.IncreaseStockGrpc
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class IncreaseStockController(
    @GrpcClient("global")
    private val grpcService: IncreaseStockGrpc.IncreaseStockBlockingStub,
) : ProductController {
    @PatchMapping("/api/products/{id}/increase-stock")
    @Operation(
        operationId = "increaseStock",
    )
    fun handle(
        @PathVariable("id")
        id: String,
        @RequestBody
        request: IncreaseStockHttpRequest,
    ) {
        val grpcRequest = request.toGrpcRequest(id)

        grpcService.handle(grpcRequest)
    }
}
