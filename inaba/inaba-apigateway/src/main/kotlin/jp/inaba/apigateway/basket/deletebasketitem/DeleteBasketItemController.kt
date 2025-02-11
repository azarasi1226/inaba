package jp.inaba.apigateway.basket.deletebasketitem

import jp.inaba.apigateway.basket.BasketController
import jp.inaba.grpc.basket.DeleteBasketItemGrpc
import jp.inaba.grpc.basket.DeleteBasketItemRequest
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class DeleteBasketItemController(
    @GrpcClient("global")
    private val grpcService: DeleteBasketItemGrpc.DeleteBasketItemBlockingStub,
) : BasketController {
    @DeleteMapping("/api/baskets/{id}/items/{productId}")
    fun handle(
        @PathVariable("id")
        id: String,
        @PathVariable("productId")
        productId: String,
    ) {
        val grpcRequest =
            DeleteBasketItemRequest.newBuilder()
                .setId(id)
                .setProductId(productId)
                .build()

        grpcService.handle(grpcRequest)
    }
}
