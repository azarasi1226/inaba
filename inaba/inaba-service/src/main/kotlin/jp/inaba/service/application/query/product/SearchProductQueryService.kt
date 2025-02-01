package jp.inaba.service.application.query.product

import jakarta.persistence.EntityManager
import jp.inaba.core.domain.common.Page
import jp.inaba.core.domain.common.Paging
import jp.inaba.core.domain.common.PagingCondition
import jp.inaba.message.product.query.SearchProductQuery
import jp.inaba.message.product.query.SearchProductResult
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class SearchProductQueryService(
    private val entityManager: EntityManager
) {
    companion object {
        //TODO (WHERない)
        private val QUERY =
"""
SELECT
    p.id AS ${SearchProductSqlResult::id.name},
    p.name AS ${SearchProductSqlResult::name.name},
    p.image_url AS ${SearchProductSqlResult::imageUrl.name},
    p.price AS ${SearchProductSqlResult::price.name},
    p.quantity AS ${SearchProductSqlResult::quantity.name},
    COUNT(*) OVER() AS ${SearchProductSqlResult::totalCount.name}
FROM product p
ORDER BY :sortProperty :sortDirection
LIMIT :offset, :pageSize
"""
    }

    @QueryHandler
    fun handle(query: SearchProductQuery) : SearchProductResult {
       val nativeQuery =
           entityManager.createNativeQuery(QUERY, SearchProductSqlResult::class.java)
              // .setParameter("name", query.productName.value)
               .setParameter("offset", query.pagingCondition.offset)
               .setParameter("pageSize", query.pagingCondition.pageSize)
               .setParameter("sortProperty", query.sortCondition.property.name)
               .setParameter("sortDirection", query.sortCondition.direction.name)
        @Suppress("UNCHECKED_CAST")
        val result =
            convertToQueryResult(
                results = nativeQuery.resultList as List<SearchProductSqlResult>,
                pagingCondition = query.pagingCondition
            )

        return result
    }

    private fun convertToQueryResult(
        results: List<SearchProductSqlResult>,
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
