package jp.inaba.service.presentation.basket

import com.github.michaelbull.result.mapBoth
import io.grpc.Status
import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.basket.FindBasketByIdError
import jp.inaba.core.domain.common.PagingCondition
import jp.inaba.grpc.basket.BasketItem
import jp.inaba.grpc.basket.GetBasketGrpc
import jp.inaba.grpc.basket.GetBasketRequest
import jp.inaba.grpc.basket.GetBasketResponse
import jp.inaba.grpc.common.Paging
import jp.inaba.message.basket.findBasketById
import jp.inaba.message.basket.query.FindBasketByIdQuery
import jp.inaba.message.basket.query.FindBasketByIdResult
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.queryhandling.QueryGateway

@GrpcService
class GetBasketGrpcService(
    private val queryGateway: QueryGateway,
) : GetBasketGrpc.GetBasketImplBase() {
    override fun handle(
        request: GetBasketRequest,
        responseObserver: StreamObserver<GetBasketResponse>,
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

        queryGateway.findBasketById(query)
            .mapBoth(
                success = { success(it, responseObserver) },
                failure = {},
            )
    }

    private fun success(
        result: FindBasketByIdResult,
        responseObserver: StreamObserver<GetBasketResponse>,
    ) {
        val response =
            GetBasketResponse.newBuilder()
                .setPaging(
                    Paging.newBuilder()
                        .setTotalCount(result.page.paging.totalCount)
                        .setPageSize(result.page.paging.pageSize)
                        .setPageNumber(result.page.paging.pageNumber),
                )
                .addAllBasketItems(
                    result.page.items.map {
                        BasketItem.newBuilder()
                            .setProductId(it.productId)
                            .setProductName(it.productName)
                            .setProductPrice(it.productPrice)
                            .setProductImageUrl(it.productImageUrl)
                            .setProductQuantity(it.productQuantity)
                            .build()
                    },
                )
                .build()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

    private fun failure(
        error: FindBasketByIdError,
        responseObserver: StreamObserver<GetBasketResponse>,
    ) {
        val status =
            when (error) {
                FindBasketByIdError.BASKET_NOT_FOUND -> Status.NOT_FOUND
            }
        responseObserver.onError(status.asException())
    }
}
