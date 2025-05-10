package jp.inaba.service.presentation.product

import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.common.PagingCondition
import jp.inaba.core.domain.product.SearchProductSortCondition
import jp.inaba.grpc.common.Paging
import jp.inaba.grpc.product.SearchProductsGrpc
import jp.inaba.grpc.product.SearchProductsRequest
import jp.inaba.grpc.product.SearchProductsResponse
import jp.inaba.grpc.product.Summary
import jp.inaba.message.product.query.SearchProductsQuery
import jp.inaba.message.product.query.SearchProductsResult
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.extensions.kotlin.query
import org.axonframework.queryhandling.QueryGateway

@GrpcService
class SearchProductsGrpcService(
    private val queryGateway: QueryGateway,
) : SearchProductsGrpc.SearchProductsImplBase() {
    override fun handle(
        request: SearchProductsRequest,
        responseObserver: StreamObserver<SearchProductsResponse>,
    ) {
        val query =
            SearchProductsQuery(
                likeProductName = request.likeProductName,
                pagingCondition =
                    PagingCondition(
                        pageSize = request.pagingCondition.pageSize,
                        pageNumber = request.pagingCondition.pageNumber,
                    ),
                sortCondition = SearchProductSortCondition.valueOf(request.sortCondition)
            )

        val result = queryGateway.query<SearchProductsResult, SearchProductsQuery>(query).get()

        val response =
            SearchProductsResponse.newBuilder()
                .setPaging(
                    Paging.newBuilder()
                        .setTotalCount(result.page.paging.totalCount)
                        .setTotalPage(result.page.paging.totalPage)
                        .setPageSize(result.page.paging.pageSize)
                        .setPageNumber(result.page.paging.pageNumber)
                        .build(),
                )
                .addAllItems(
                    result.page.items.map {
                        Summary.newBuilder()
                            .setId(it.id)
                            .setName(it.name)
                            .apply { if (it.imageUrl != null) setImageUrl(it.imageUrl) }
                            .setPrice(it.price)
                            .setQuantity(it.quantity)
                            .build()
                    },
                )
                .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}
