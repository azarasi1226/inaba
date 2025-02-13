package jp.inaba.service.application.query.basket

import jakarta.persistence.EntityManager
import jp.inaba.core.domain.common.Page
import jp.inaba.core.domain.common.Paging
import jp.inaba.core.domain.common.PagingCondition
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.core.domain.product.FindProductByIdError
import jp.inaba.message.basket.query.FindBasketByIdQuery
import jp.inaba.message.basket.query.FindBasketByIdResult
import jp.inaba.service.infrastructure.jpa.lookupbasket.LookupBasketJpaRepository
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class FindBasketByIdQueryService(
    private val entityManager: EntityManager,
    private val lookUpBasketRepository: LookupBasketJpaRepository,
) {
    companion object {
        private val QUERY =
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
LIMIT :offset, :pageSize    
"""
    }

    @QueryHandler
    fun handle(query: FindBasketByIdQuery): FindBasketByIdResult {
        lookUpBasketRepository.findById(query.basketId.value)
            .orElseThrow { UseCaseException(FindProductByIdError.PRODUCT_NOT_FOUND) }

        val nativeQuery =
            entityManager.createNativeQuery(QUERY, SqlResult::class.java)
                .setParameter("basketId", query.basketId.value)
                .setParameter("offset", query.pagingCondition.offset)
                .setParameter("pageSize", query.pagingCondition.pageSize)

        val results = nativeQuery.resultList.filterIsInstance<SqlResult>()

        return convertToQueryResult(
            results = results,
            pagingCondition = query.pagingCondition,
        )
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
                                productQuantity = it.quantity,
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
        val productPictureUrl: String,
        val quantity: Int,
        val totalCount: Long,
    )
}
