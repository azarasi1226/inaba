package jp.inaba.basket.service.application.query.basket.findbyid

import jakarta.persistence.EntityManager
import jp.inaba.basket.api.domain.basket.FindBasketByIdQuery
import jp.inaba.basket.api.domain.basket.FindBasketByIdResult
import jp.inaba.basket.api.domain.basket.FindBasketByIdSummary
import jp.inaba.common.domain.shared.Page
import jp.inaba.common.domain.shared.Paging
import jp.inaba.common.domain.shared.PagingCondition
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class FindBasketByIdQueryService(
    private val entityManager: EntityManager,
) {
    companion object {
        private val QUERY =
"""
SELECT
    p.id AS ${FindBasketByIdSqlResult::itemId.name},
    p.name AS ${FindBasketByIdSqlResult::itemName.name},
    p.price AS ${FindBasketByIdSqlResult::itemPrice.name},
    p.image_url AS ${FindBasketByIdSqlResult::itemPictureUrl.name},
    bi.item_quantity AS ${FindBasketByIdSqlResult::itemQuantity.name},
    COUNT(*) OVER() AS ${FindBasketByIdSqlResult::totalCount.name}
FROM basket_item bi
INNER JOIN product p
    ON bi.basket_id = :basketId
    AND bi.product_id = p.id
LIMIT :offset, :pageSize    
"""
    }

    @QueryHandler
    fun handle(query: FindBasketByIdQuery): FindBasketByIdResult {
        val nativeQuery =
            entityManager.createNativeQuery(QUERY, FindBasketByIdSqlResult::class.java)
                .setParameter("basketId", query.basketId.value)
                .setParameter("offset", query.pagingCondition.offset)
                .setParameter("pageSize", query.pagingCondition.pageSize)

        @Suppress("UNCHECKED_CAST")
        return convertToQueryResult(
            results = nativeQuery.resultList as List<FindBasketByIdSqlResult>,
            pagingCondition = query.pagingCondition,
        )
    }

    private fun convertToQueryResult(
        results: List<FindBasketByIdSqlResult>,
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
                            FindBasketByIdSummary(
                                itemId = it.itemId,
                                itemName = it.itemName,
                                itemPrice = it.itemPrice,
                                itemPictureUrl = it.itemPictureUrl,
                                itemQuantity = it.itemQuantity,
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
}
