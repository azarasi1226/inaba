package jp.inaba.apigateway.presentation.brand.search

import jp.inaba.apigateway.presentation.common.Page
import jp.inaba.apigateway.presentation.common.Paging
import jp.inaba.grpc.brand.SearchBrandsResponse

data class SearchBrandsHttpResponse(
    val page: Page<Summary>,
) {
    data class Summary(
        val id: String,
        val name: String,
    )

    companion object {
        fun fromGrpcResponse(grpcResponse: SearchBrandsResponse): SearchBrandsHttpResponse =
            SearchBrandsHttpResponse(
                page =
                    Page(
                        items =
                            grpcResponse.itemsList.map {
                                Summary(
                                    id = it.id,
                                    name = it.name,
                                )
                            },
                        paging =
                            Paging(
                                totalCount = grpcResponse.paging.totalCount,
                                totalPage = grpcResponse.paging.totalPage,
                                pageSize = grpcResponse.paging.pageSize,
                                pageNumber = grpcResponse.paging.pageNumber,
                            ),
                    ),
            )
    }
}
