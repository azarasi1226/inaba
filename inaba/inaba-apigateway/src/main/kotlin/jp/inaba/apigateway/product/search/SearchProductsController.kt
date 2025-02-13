package jp.inaba.apigateway.product.search

import jp.inaba.apigateway.product.ProductController
import jp.inaba.grpc.common.PagingCondition
import jp.inaba.grpc.common.SortCondition
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
    fun handle(
        @RequestParam("name")
        name: String,
        @RequestParam("pageSize")
        pageSize: Int,
        @RequestParam("pageNumber")
        pageNumber: Int,
        @RequestParam("sortProperty")
        sortProperty: String,
        @RequestParam("sortDirection")
        sortDirection: String,
    ): SearchProductsHttpResponse {
        val grpcRequest =
            SearchProductsRequest.newBuilder()
                .setName(name)
                .setPagingCondition(
                    PagingCondition.newBuilder()
                        .setPageSize(pageSize)
                        .setPageNumber(pageNumber)
                        .build(),
                )
                .setSortCondition(
                    SortCondition.newBuilder()
                        .setProperty(sortProperty)
                        .setDirection(sortDirection)
                        .build(),
                )
                .build()

        val grpcResponse = grpcService.handle(grpcRequest)

        return SearchProductsHttpResponse.fromGrpcResponse(grpcResponse)
    }
}
