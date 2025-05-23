package jp.inaba.apigateway.presentation.product.search

import io.swagger.v3.oas.annotations.Operation
import jp.inaba.apigateway.presentation.product.ProductController
import jp.inaba.grpc.common.PagingCondition
import jp.inaba.grpc.product.SearchProductsGrpc
import jp.inaba.grpc.product.SearchProductsRequest
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchProductsController(
    @GrpcClient("global")
    private val grpcService: SearchProductsGrpc.SearchProductsBlockingStub,
) : ProductController {
    @GetMapping("/api/products")
    @Operation(
        operationId = "searchProduct",
    )
    fun handle(
        @RequestParam("likeProductName")
        likeProductName: String,
        @RequestParam("pageSize")
        pageSize: Int,
        @RequestParam("pageNumber")
        pageNumber: Int,
        @RequestParam("sortCondition")
        sortCondition: SearchProductSortCondition,
    ): SearchProductsHttpResponse {
        val grpcRequest =
            SearchProductsRequest.newBuilder()
                .setLikeProductName(likeProductName)
                .setPagingCondition(
                    PagingCondition.newBuilder()
                        .setPageSize(pageSize)
                        .setPageNumber(pageNumber)
                        .build(),
                )
                .setSortCondition(sortCondition.name)
                .build()

        val grpcResponse = grpcService.handle(grpcRequest)

        return SearchProductsHttpResponse.fromGrpcResponse(grpcResponse)
    }
}
