package jp.inaba.service.presentation.brand

import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.common.PagingCondition
import jp.inaba.grpc.brand.SearchBrandsGrpc
import jp.inaba.grpc.brand.SearchBrandsRequest
import jp.inaba.grpc.brand.SearchBrandsResponse
import jp.inaba.grpc.brand.Summary
import jp.inaba.grpc.common.Paging
import jp.inaba.message.brand.query.SearchBrandsQuery
import jp.inaba.message.brand.query.SearchBrandsResult
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.extensions.kotlin.query
import org.axonframework.queryhandling.QueryGateway

@GrpcService
class SearchBrandsGrpcService(
    private val queryGateway: QueryGateway,
) : SearchBrandsGrpc.SearchBrandsImplBase() {
    override fun handle(
        request: SearchBrandsRequest,
        responseObserver: StreamObserver<SearchBrandsResponse>,
    ) {
        val query =
            SearchBrandsQuery(
                likeBrandName = request.likeBrandName,
                pagingCondition =
                    PagingCondition(
                        pageSize = request.pagingCondition.pageSize,
                        pageNumber = request.pagingCondition.pageNumber,
                    ),
            )

        val result = queryGateway.query<SearchBrandsResult, SearchBrandsQuery>(query).get()

        val response =
            SearchBrandsResponse.newBuilder()
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
                            .build()
                    },
                )
                .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}
