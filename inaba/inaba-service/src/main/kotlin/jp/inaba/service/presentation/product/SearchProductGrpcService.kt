package jp.inaba.service.presentation.product

import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.common.PagingCondition
import jp.inaba.core.domain.common.SortCondition
import jp.inaba.core.domain.common.SortDirection
import jp.inaba.core.domain.product.ProductName
import jp.inaba.core.domain.product.ProductSortProperty
import jp.inaba.grpc.common.Paging
import jp.inaba.grpc.product.SearchProductGrpc
import jp.inaba.grpc.product.SearchProductRequest
import jp.inaba.grpc.product.SearchProductResponse
import jp.inaba.grpc.product.Summary
import jp.inaba.message.product.query.SearchProductQuery
import jp.inaba.message.product.query.SearchProductResult
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.extensions.kotlin.query
import org.axonframework.queryhandling.QueryGateway

@GrpcService
class SearchProductGrpcService(
    private val queryGateway: QueryGateway,
) : SearchProductGrpc.SearchProductImplBase() {
    override fun handle(request: SearchProductRequest, responseObserver: StreamObserver<SearchProductResponse>) {
        val query = SearchProductQuery(
            productName = ProductName(request.name),
            pagingCondition = PagingCondition(
                pageSize = request.pagingCondition.pageSize,
                pageNumber = request.pagingCondition.pageNumber
            ),
            sortCondition = SortCondition(
                property = ProductSortProperty.valueOf(request.sortCondition.property),
                direction = SortDirection.valueOf(request.sortCondition.direction)
            )
        )

        val result = queryGateway.query<SearchProductResult, SearchProductQuery>(query).get()

        val response = SearchProductResponse.newBuilder()
            .setPaging(
                Paging.newBuilder()
                    .setTotalCount(result.page.paging.totalCount)
                    .setPageSize(result.page.paging.pageSize)
                    .setPageNumber(result.page.paging.pageNumber)
                    .build()
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
                }
            )
            .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}