package jp.inaba.apigateway.basket.deletebasketitem

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jp.inaba.apigateway.AuthenticatedUserService
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
    private val authenticatedUserService: AuthenticatedUserService,
) : BasketController {
    @DeleteMapping("/api/baskets/items/{productId}")
    @Operation(
        security = [
            SecurityRequirement(name = "OIDC"),
        ],
    )
    fun handle(
        @PathVariable("productId")
        productId: String,
    ) {
        val basketId = authenticatedUserService.getUserMetadata().basketId
        val grpcRequest =
            DeleteBasketItemRequest.newBuilder()
                .setId(basketId)
                .setProductId(productId)
                .build()

        grpcService.handle(grpcRequest)
    }
}
