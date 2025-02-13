package jp.inaba.service.presentation.product

import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.common.PagingCondition
import jp.inaba.core.domain.common.SortCondition
import jp.inaba.core.domain.common.SortDirection
import jp.inaba.core.domain.product.ProductSortProperty
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
                likeProductName = request.name,
                pagingCondition =
                    PagingCondition(
                        pageSize = request.pagingCondition.pageSize,
                        pageNumber = request.pagingCondition.pageNumber,
                    ),
                sortCondition =
                    SortCondition(
                        property = ProductSortProperty.valueOf(request.sortCondition.property),
                        direction = SortDirection.valueOf(request.sortCondition.direction),
                    ),
            )

        val result = queryGateway.query<SearchProductsResult, SearchProductsQuery>(query).get()

        val response =
            SearchProductsResponse.newBuilder()
                .setPaging(
                    Paging.newBuilder()
                        .setTotalCount(result.page.paging.totalCount)
                        .setPageSize(result.page.paging.pageSize)
                        .setPageNumber(result.page.paging.pageNumber)
                        .build(),
                )
                .addAllItems(
                    result.page.items.map {
                        Summary.newBuilder()
                            .setId(it.id)
                            .setName(it.name)
                            .setImageUrl(it.imageUrl)
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
