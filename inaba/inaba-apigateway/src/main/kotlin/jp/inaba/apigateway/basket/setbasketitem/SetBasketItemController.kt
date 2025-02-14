package jp.inaba.apigateway.basket.setbasketitem

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jp.inaba.apigateway.AuthenticatedUserService
import jp.inaba.apigateway.basket.BasketController
import jp.inaba.grpc.basket.SetBasketItemGrpc
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SetBasketItemController(
    @GrpcClient("global")
    private val grpcService: SetBasketItemGrpc.SetBasketItemBlockingStub,
    private val authenticatedUserService: AuthenticatedUserService,
) : BasketController {
    @PostMapping("/api/baskets")
    @Operation(
        security = [
            SecurityRequirement(name = "OIDC"),
        ],
    )
    fun handle(
        @RequestBody
        request: SetBasketItemHttpRequest,
    ) {
        val basketId = authenticatedUserService.getUserMetadata().basketId
        val grpcRequest = request.toGrpcRequest(basketId)

        grpcService.handle(grpcRequest)
    }
}
