package jp.inaba.service.presentation.basket

import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.common.PagingCondition
import jp.inaba.grpc.basket.BasketItem
import jp.inaba.grpc.basket.FindBasketByIdGrpc
import jp.inaba.grpc.basket.FindBasketByIdRequest
import jp.inaba.grpc.basket.FindBasketByIdResponse
import jp.inaba.grpc.common.Paging
import jp.inaba.message.basket.query.FindBasketByIdQuery
import jp.inaba.message.basket.query.FindBasketByIdResult
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.extensions.kotlin.query
import org.axonframework.queryhandling.QueryGateway

@GrpcService
class FindBasketByIdGrpcService(
    private val queryGateway: QueryGateway,
) : FindBasketByIdGrpc.FindBasketByIdImplBase() {
    override fun handle(
        request: FindBasketByIdRequest,
        responseObserver: StreamObserver<FindBasketByIdResponse>,
    ) {
        val query =
            FindBasketByIdQuery(
                basketId = BasketId(request.id),
                pagingCondition =
                    PagingCondition(
                        pageSize = request.pagingCondition.pageSize,
                        pageNumber = request.pagingCondition.pageNumber,
                    ),
            )

        val result = queryGateway.query<FindBasketByIdResult, FindBasketByIdQuery>(query).get()

        val response =
            FindBasketByIdResponse.newBuilder()
                .setPaging(
                    Paging.newBuilder()
                        .setTotalCount(result.page.paging.totalCount)
                        .setPageSize(result.page.paging.pageSize)
                        .setPageNumber(result.page.paging.pageNumber)
                        .build(),
                )
                .addAllBasketItems(
                    result.page.items.map {
                        BasketItem.newBuilder()
                            .setProductId(it.productId)
                            .setProductName(it.productName)
                            .setProductPrice(it.productPrice)
                            .setProductImageUrl(it.productImageUrl)
                            .setBasketItemQuantity(it.basketItemQuantity)
                            .build()
                    },
                )
                .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}
