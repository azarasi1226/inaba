package jp.inaba.apigateway.basket.getbasket

import jp.inaba.apigateway.basket.BasketController
import jp.inaba.grpc.basket.GetBasketGrpc
import jp.inaba.grpc.basket.GetBasketRequest
import jp.inaba.grpc.common.PagingCondition
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GetBasketController(
    @GrpcClient("global")
    private val grpcService: GetBasketGrpc.GetBasketBlockingStub
) : BasketController {
    @GetMapping("/api/baskets/{id}")
    fun handle(
        @PathVariable("id")
        id: String,
        @RequestParam("pageSize")
        pageSize: Int,
        @RequestParam("pageNumber")
        pageNumber: Int
    ): GetBasketHttpResponse {
        val grpcRequest =
            GetBasketRequest.newBuilder()
                .setPagingCondition(
                    PagingCondition.newBuilder()
                        .setPageSize(pageSize)
                        .setPageNumber(pageNumber)
                        .build()
                )
                .setId(id)
                .build()

        val grpcResponse = grpcService.handle(grpcRequest)

        return GetBasketHttpResponse.formGrpcResponse(grpcResponse)
    }
}