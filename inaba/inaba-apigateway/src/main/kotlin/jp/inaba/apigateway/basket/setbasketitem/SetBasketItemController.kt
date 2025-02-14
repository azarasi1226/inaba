package jp.inaba.apigateway.basket.setbasketitem

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jp.inaba.apigateway.basket.BasketController
import jp.inaba.grpc.basket.SetBasketItemGrpc
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SetBasketItemController(
    @GrpcClient("global")
    private val grpcService: SetBasketItemGrpc.SetBasketItemBlockingStub,
) : BasketController {
    @PostMapping("/api/baskets/{id}")
    @Operation(
        security = [
            SecurityRequirement(name = "OIDC")
        ]
    )
    fun handle(
        @PathVariable("id")
        id: String,
        @RequestBody
        request: SetBasketItemHttpRequest,
    ) {
        val grpcRequest = request.toGrpcRequest(id)

        grpcService.handle(grpcRequest)
    }
}
