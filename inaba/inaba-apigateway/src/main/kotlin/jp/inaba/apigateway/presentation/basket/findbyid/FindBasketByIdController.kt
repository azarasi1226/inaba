package jp.inaba.apigateway.presentation.basket.findbyid

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jp.inaba.apigateway.application.AuthenticatedUserService
import jp.inaba.apigateway.presentation.basket.BasketController
import jp.inaba.grpc.basket.FindBasketByIdGrpc
import jp.inaba.grpc.basket.FindBasketByIdRequest
import jp.inaba.grpc.common.PagingCondition
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class FindBasketByIdController(
    @GrpcClient("global")
    private val grpcService: FindBasketByIdGrpc.FindBasketByIdBlockingStub,
    private val authenticatedUserService: AuthenticatedUserService,
) : BasketController {
    @GetMapping("/api/baskets")
    @Operation(
        security = [
            SecurityRequirement(name = "OIDC"),
        ],
    )
    fun handle(
        @RequestParam("pageSize")
        pageSize: Int,
        @RequestParam("pageNumber")
        pageNumber: Int,
    ): FindBasketByIdHttpResponse {
        val basketId = authenticatedUserService.getUserMetadata().basketId
        val grpcRequest =
            FindBasketByIdRequest.newBuilder()
                .setPagingCondition(
                    PagingCondition.newBuilder()
                        .setPageSize(pageSize)
                        .setPageNumber(pageNumber)
                        .build(),
                )
                .setId(basketId)
                .build()

        val grpcResponse = grpcService.handle(grpcRequest)

        return FindBasketByIdHttpResponse.formGrpcResponse(grpcResponse)
    }
}
