package jp.inaba.service2.feature.query.basket.findbyid

import io.grpc.stub.StreamObserver
import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.common.PagingCondition
import jp.inaba.grpc.basket.FindBasketByIdGrpc
import jp.inaba.grpc.basket.FindBasketByIdRequest
import jp.inaba.grpc.basket.FindBasketByIdResponse
import jp.inaba.grpc.common.Paging
import jp.inaba.message.basket.query.FindBasketByIdQuery
import jp.inaba.message.basket.query.findBasketById
import jp.inaba.message.getOrThrow
import net.devh.boot.grpc.server.service.GrpcService
import org.axonframework.messaging.queryhandling.gateway.QueryGateway

@GrpcService
class FindBasketByIdGrpcService(
    private val queryGateway: QueryGateway,
) : FindBasketByIdGrpc.FindBasketByIdImplBase() {
    override fun handle(
        request: FindBasketByIdRequest,
        responseObserver: StreamObserver<FindBasketByIdResponse>,
    ) {
        val query = FindBasketByIdQuery(
            basketId = BasketId(request.id),
            pagingCondition = PagingCondition(
                pageSize = request.pagingCondition.pageSize,
                pageNumber = request.pagingCondition.pageNumber,
            ),
        )
        val result = queryGateway.findBasketById(query).getOrThrow()
        val page = result.page

        val response = FindBasketByIdResponse.newBuilder()
            .addAllBasketItems(
                page.items.map { item ->
                    val builder = jp.inaba.grpc.basket.BasketItem.newBuilder()
                        .setProductId(item.productId)
                        .setProductName(item.productName)
                        .setProductPrice(item.productPrice)
                        .setBasketItemQuantity(item.basketItemQuantity)
                    if (item.productImageUrl != null) {
                        builder.setProductImageUrl(item.productImageUrl)
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
