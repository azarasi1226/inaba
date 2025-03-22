package jp.inaba.apigateway.presentation.product.decreasestock

import io.swagger.v3.oas.annotations.Operation
import jp.inaba.apigateway.presentation.product.ProductController
import jp.inaba.grpc.stock.DecreaseStockGrpc
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class DecreaseStockController(
    @GrpcClient("global")
    private val grpcService: DecreaseStockGrpc.DecreaseStockBlockingStub,
) : ProductController {
    @PatchMapping("/api/products/{id}/decrease-stock")
    @Operation(
        operationId = "decreaseStock",
    )
    fun handle(
        @PathVariable("id")
        id: String,
        @RequestBody
        request: DecreaseStockHttpRequest,
    ) {
        val grpcRequest = request.toGrpcRequest(id)

        grpcService.handle(grpcRequest)
    }
}
