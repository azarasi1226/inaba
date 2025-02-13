package jp.inaba.apigateway.basket.findbyid

import jp.inaba.apigateway.basket.BasketController
import jp.inaba.grpc.basket.FindBasketByIdGrpc
import jp.inaba.grpc.basket.FindBasketByIdRequest
import jp.inaba.grpc.common.PagingCondition
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class FindBasketByIdController(
    @GrpcClient("global")
    private val grpcService: FindBasketByIdGrpc.FindBasketByIdBlockingStub,
) : BasketController {
    @GetMapping("/api/baskets/{id}")
    fun handle(
        @PathVariable("id")
        id: String,
        @RequestParam("pageSize")
        pageSize: Int,
        @RequestParam("pageNumber")
        pageNumber: Int,
    ): FindBasketByIdHttpResponse {
        val grpcRequest =
            FindBasketByIdRequest.newBuilder()
                .setPagingCondition(
                    PagingCondition.newBuilder()
                        .setPageSize(pageSize)
                        .setPageNumber(pageNumber)
                        .build(),
                )
                .setId(id)
                .build()

        val grpcResponse = grpcService.handle(grpcRequest)

        return FindBasketByIdHttpResponse.formGrpcResponse(grpcResponse)
    }
}
