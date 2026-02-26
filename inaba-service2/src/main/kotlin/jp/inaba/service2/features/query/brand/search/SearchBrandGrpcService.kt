package jp.inaba.service2.features.query.brand.search

import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.common.PagingCondition
import jp.inaba.grpc.brand.SearchBrandsGrpc
import jp.inaba.grpc.brand.SearchBrandsRequest
import jp.inaba.grpc.brand.SearchBrandsResponse
import jp.inaba.grpc.brand.Summary
import jp.inaba.grpc.common.Paging
import jp.inaba.message.brand.query.SearchBrandsQuery
import jp.inaba.message.brand.query.searchBrands
import jp.inaba.message.getOrThrow
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.messaging.queryhandling.gateway.QueryGateway

@GrpcService
class SearchBrandGrpcService(
    private val queryGateway: QueryGateway,
) : SearchBrandsGrpc.SearchBrandsImplBase() {
    override fun handle(
        request: SearchBrandsRequest,
        responseObserver: StreamObserver<SearchBrandsResponse>,
    ) {
        val query = SearchBrandsQuery(
            likeBrandName = request.likeBrandName,
            pagingCondition = PagingCondition(
                pageSize = request.pagingCondition.pageSize,
                pageNumber = request.pagingCondition.pageNumber,
            ),
        )

        val page = queryGateway.searchBrands(query).getOrThrow()

        val response = SearchBrandsResponse.newBuilder()
            .addAllItems(
                page.items.map {
                    Summary.newBuilder()
                        .setId(it.id)
                        .setName(it.name)
                        .build()
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
