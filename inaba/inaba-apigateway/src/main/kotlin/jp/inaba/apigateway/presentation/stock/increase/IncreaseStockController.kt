package jp.inaba.apigateway.presentation.stock.increase

import jp.inaba.apigateway.presentation.stock.StockController
import jp.inaba.grpc.stock.IncreaseStockGrpc
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class IncreaseStockController(
    @GrpcClient("global")
    private val grpcService: IncreaseStockGrpc.IncreaseStockBlockingStub,
) : StockController {
    @PatchMapping("/api/stocks/{id}/increase")
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
