package jp.inaba.service2.feature.query.product.search

import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.common.PagingCondition
import jp.inaba.core.domain.product.SearchProductSortCondition
import jp.inaba.grpc.common.Paging
import jp.inaba.grpc.product.SearchProductsGrpc
import jp.inaba.grpc.product.SearchProductsRequest
import jp.inaba.grpc.product.SearchProductsResponse
import jp.inaba.grpc.product.Summary
import jp.inaba.message.getOrThrow
import jp.inaba.message.product.query.SearchProductsQuery
import jp.inaba.message.product.query.searchProducts
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.messaging.queryhandling.gateway.QueryGateway

@GrpcService
class SearchProductsGrpcService(
    private val queryGateway: QueryGateway,
) : SearchProductsGrpc.SearchProductsImplBase() {
    override fun handle(
        request: SearchProductsRequest,
        responseObserver: StreamObserver<SearchProductsResponse>,
    ) {
        val query = SearchProductsQuery(
            likeProductName = request.likeProductName,
            pagingCondition = PagingCondition(
                pageSize = request.pagingCondition.pageSize,
                pageNumber = request.pagingCondition.pageNumber,
            ),
            sortCondition = SearchProductSortCondition.valueOf(request.sortCondition),
        )

        val page = queryGateway.searchProducts(query).getOrThrow()

        val response = SearchProductsResponse.newBuilder()
            .addAllItems(
                page.items.map { item ->
                    val builder = Summary.newBuilder()
                        .setId(item.id)
                        .setName(item.name)
                        .setPrice(item.price)
                        .setQuantity(item.quantity)
                    if (item.imageUrl != null) {
                        builder.setImageUrl(item.imageUrl)
                    }
                    builder.build()
                },
            )
            .setPaging(
                Paging.newBuilder()
                    .setTotalCount(page.paging.totalCount)
                    .setTotalPage(page.paging.totalPage)
                    .setPageSize(page.paging.pageSize)
                    .setPageNumber(page.paging.pageNumber)
                    .build(),
            )
            .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}
