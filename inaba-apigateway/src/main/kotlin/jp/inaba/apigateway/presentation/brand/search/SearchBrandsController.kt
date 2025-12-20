package jp.inaba.apigateway.presentation.brand.search

import io.swagger.v3.oas.annotations.Operation
import jp.inaba.apigateway.presentation.brand.BrandController
import jp.inaba.grpc.brand.SearchBrandsGrpc
import jp.inaba.grpc.brand.SearchBrandsRequest
import jp.inaba.grpc.common.PagingCondition
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchBrandsController(
    @GrpcClient("global")
    private val grpcService: SearchBrandsGrpc.SearchBrandsBlockingStub,
) : BrandController {
    @GetMapping("/api/brands")
    @Operation(
        operationId = "searchBrands",
    )
    fun handle(
        @RequestParam("likeBrandName")
        likeBrandName: String,
        @RequestParam("pageSize")
        pageSize: Int,
        @RequestParam("pageNumber")
        pageNumber: Int,
    ): SearchBrandsHttpResponse {
        val grpcRequest =
            SearchBrandsRequest
                .newBuilder()
                .setLikeBrandName(likeBrandName)
                .setPagingCondition(
                    PagingCondition
                        .newBuilder()
                        .setPageSize(pageSize)
                        .setPageNumber(pageNumber)
                        .build(),
                ).build()

        val grpcResponse = grpcService.handle(grpcRequest)

        return SearchBrandsHttpResponse.fromGrpcResponse(grpcResponse)
    }
}
