package jp.inaba.service.application.query.basket

import jakarta.persistence.EntityManager
import jakarta.persistence.Query
import jp.inaba.core.domain.basket.FindBasketByIdError
import jp.inaba.core.domain.common.Page
import jp.inaba.core.domain.common.Paging
import jp.inaba.core.domain.common.PagingCondition
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.message.basket.query.FindBasketByIdQuery
import jp.inaba.message.basket.query.FindBasketByIdResult
import jp.inaba.service.infrastructure.jpa.lookupbasket.LookupBasketJpaRepository
import jp.inaba.service.utlis.sqlDebugLogging
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class FindBasketByIdQueryService(
    private val entityManager: EntityManager,
    private val lookUpBasketRepository: LookupBasketJpaRepository,
) {
    @QueryHandler
    fun handle(query: FindBasketByIdQuery): FindBasketByIdResult {
        lookUpBasketRepository
            .findById(query.basketId.value)
            .orElseThrow { UseCaseException(FindBasketByIdError.BASKET_NOT_FOUND) }

        val nativeQuery = createNativeQuery(query)

        val results = nativeQuery.resultList.filterIsInstance<SqlResult>()

        return convertToQueryResult(
            results = results,
            pagingCondition = query.pagingCondition,
        )
    }

    private fun createNativeQuery(query: FindBasketByIdQuery): Query {
        val sql =
"""
SELECT
    p.id AS ${SqlResult::productId.name},
    p.name AS ${SqlResult::productName.name},
    p.price AS ${SqlResult::productPrice.name},
    p.image_url AS ${SqlResult::productPictureUrl.name},
    b.item_quantity AS ${SqlResult::quantity.name},
    COUNT(*) OVER() AS ${SqlResult::totalCount.name}
FROM basket b
INNER JOIN product p
    ON b.basket_id = :basketId
    AND b.product_id = p.id
ORDER BY added_at ASC
LIMIT :offset, :pageSize    
"""

        val nativeQuery =
            entityManager
                .createNativeQuery(sql, SqlResult::class.java)
                .setParameter("basketId", query.basketId.value)
                .setParameter("offset", query.pagingCondition.offset)
                .setParameter("pageSize", query.pagingCondition.pageSize)

        nativeQuery.sqlDebugLogging()

        return nativeQuery
    }

    private fun convertToQueryResult(
        results: List<SqlResult>,
        pagingCondition: PagingCondition,
    ): FindBasketByIdResult {
        val totalCount =
            if (results.isNotEmpty()) {
                results.first().totalCount
            } else {
                0
            }

        return FindBasketByIdResult(
            page =
                Page(
                    items =
                        results.map {
                            FindBasketByIdResult.BasketItem(
                                productId = it.productId,
                                productName = it.productName,
                                productPrice = it.productPrice,
                                productImageUrl = it.productPictureUrl,
                                basketItemQuantity = it.quantity,
                            )
                        },
                    paging =
                        Paging(
                            totalCount = totalCount,
                            pageSize = pagingCondition.pageSize,
                            pageNumber = pagingCondition.pageNumber,
                        ),
                ),
        )
    }

    private data class SqlResult(
        val productId: String,
        val productName: String,
        val productPrice: Int,
        val productPictureUrl: String?,
        val quantity: Int,
        val totalCount: Long,
    )
}
