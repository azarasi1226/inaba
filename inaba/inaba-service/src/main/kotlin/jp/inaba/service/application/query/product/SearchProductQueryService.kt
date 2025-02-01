package jp.inaba.service.application.query.product

import jakarta.persistence.EntityManager
import jp.inaba.core.domain.common.Page
import jp.inaba.core.domain.common.Paging
import jp.inaba.core.domain.common.PagingCondition
import jp.inaba.message.product.query.SearchProductQuery
import jp.inaba.message.product.query.SearchProductResult
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

data class SqlResult(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val price: Int,
    val quantity: Int,
    val totalCount: Long,
)

@Component
class SearchProductQueryService(
    private val entityManager: EntityManager
) {
    companion object {
        private val QUERY =
"""
SELECT
    p.id AS ${SqlResult::id.name},
    p.name AS ${SqlResult::name.name},
    p.image_url AS ${SqlResult::imageUrl.name},
    p.price AS ${SqlResult::price.name},
    p.quantity AS ${SqlResult::quantity.name},
    COUNT(*) OVER() AS ${SqlResult::totalCount.name}
FROM product p
WHERE ${SqlResult::name.name} LIKE :likeName
ORDER BY :sortProperty :sortDirection
LIMIT :offset, :pageSize
"""
    }

    @QueryHandler
    fun handle(query: SearchProductQuery) : SearchProductResult {
       val nativeQuery =
           entityManager.createNativeQuery(QUERY, SqlResult::class.java)
               .setParameter("likeName", "%${query.likeProductName}%")
               .setParameter("offset", query.pagingCondition.offset)
               .setParameter("pageSize", query.pagingCondition.pageSize)
               .setParameter("sortProperty", query.sortCondition.property.propertyName)
               .setParameter("sortDirection", query.sortCondition.direction.name)

        val results = nativeQuery.resultList.filterIsInstance<SqlResult>()

        return convertToQueryResult(
            results = results,
            pagingCondition = query.pagingCondition
        )
    }

    private fun convertToQueryResult(
        results: List<SqlResult>,
        pagingCondition: PagingCondition,
    ): SearchProductResult {
        val totalCount =
            if (results.isNotEmpty()) {
                results.first().totalCount
            } else {
                0
            }

        return SearchProductResult(
            page =
                Page(
                    items =
                        results.map {
                            SearchProductResult.Summary(
                                id = it.id,
                                name = it.name,
                                imageUrl = it.imageUrl,
                                price = it.price,
                                quantity = it.quantity,
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
