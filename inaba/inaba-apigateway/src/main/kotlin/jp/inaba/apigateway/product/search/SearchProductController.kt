package jp.inaba.apigateway.product.search

import jp.inaba.apigateway.common.Page
import jp.inaba.apigateway.common.Paging
import jp.inaba.apigateway.product.ProductController
import jp.inaba.grpc.common.SortCondition
import jp.inaba.grpc.product.SearchProductGrpc
import jp.inaba.grpc.product.SearchProductRequest
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchProductController(
    @GrpcClient("global")
    private val grpcService: SearchProductGrpc.SearchProductBlockingStub,
) : ProductController {
    @GetMapping("/api/product")
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
    ): SearchProductResponse {
        val grpcRequest =
            SearchProductRequest.newBuilder()
                .setName(name)
                .setPagingCondition(
                    jp.inaba.grpc.common.PagingCondition.newBuilder()
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

        return SearchProductResponse(
            page =
                Page(
                    items =
                        grpcResponse.itemsList.map {
                            SearchProductResponse.Summary(
                                name = it.name,
                                quantity = it.quantity,
                                imageUrl = it.imageUrl,
                            )
                        },
                    paging =
                        Paging(
                            totalCount = grpcResponse.paging.totalCount,
                            pageSize = grpcResponse.paging.pageSize,
                            pageNumber = grpcResponse.paging.pageNumber,
                        ),
                ),
        )
    }
}
