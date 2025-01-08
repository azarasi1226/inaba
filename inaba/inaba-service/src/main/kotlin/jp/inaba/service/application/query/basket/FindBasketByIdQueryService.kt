package jp.inaba.service.application.query.basket

import jakarta.persistence.EntityManager
import jp.inaba.core.domain.common.Page
import jp.inaba.core.domain.common.Paging
import jp.inaba.core.domain.common.PagingCondition
import jp.inaba.service.infrastructure.jpa.lookupbasket.LookupBasketJpaRepository
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import java.util.Optional
import kotlin.jvm.optionals.getOrElse
import jp.inaba.message.basket.query.*

@Component
class FindBasketByIdQueryService(
    private val entityManager: EntityManager,
    private val lookUpBasketRepository: LookupBasketJpaRepository,
) {
    companion object {
        private val QUERY =
"""
SELECT
    p.id AS ${FindBasketByIdSqlResult::productId.name},
    p.name AS ${FindBasketByIdSqlResult::productName.name},
    p.price AS ${FindBasketByIdSqlResult::productPrice.name},
    p.image_url AS ${FindBasketByIdSqlResult::productPictureUrl.name},
    b.item_quantity AS ${FindBasketByIdSqlResult::quantity.name},
    COUNT(*) OVER() AS ${FindBasketByIdSqlResult::totalCount.name}
FROM basket b
INNER JOIN product p
    ON b.basket_id = :basketId
    AND b.product_id = p.id
LIMIT :offset, :pageSize    
"""
    }

    @QueryHandler
    fun handle(query: FindBasketByIdQuery): Optional<FindBasketByIdResult> {
        lookUpBasketRepository.findById(query.basketId.value)
            .getOrElse { return Optional.empty() }

        val nativeQuery =
            entityManager.createNativeQuery(QUERY, FindBasketByIdSqlResult::class.java)
                .setParameter("basketId", query.basketId.value)
                .setParameter("offset", query.pagingCondition.offset)
                .setParameter("pageSize", query.pagingCondition.pageSize)

        @Suppress("UNCHECKED_CAST")
        val result =
            convertToQueryResult(
                results = nativeQuery.resultList as List<FindBasketByIdSqlResult>,
                pagingCondition = query.pagingCondition,
            )

        return Optional.of(result)
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
}
