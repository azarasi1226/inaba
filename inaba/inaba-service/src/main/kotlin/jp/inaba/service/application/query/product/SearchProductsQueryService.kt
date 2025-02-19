package jp.inaba.service.application.query.product

import jakarta.persistence.EntityManager
import jp.inaba.core.domain.common.Page
import jp.inaba.core.domain.common.Paging
import jp.inaba.core.domain.common.PagingCondition
import jp.inaba.message.product.query.SearchProductsQuery
import jp.inaba.message.product.query.SearchProductsResult
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class SearchProductsQueryService(
    private val entityManager: EntityManager,
) {
    @QueryHandler
    fun handle(query: SearchProductsQuery): SearchProductsResult {
    val query2 =
                """
                SELECT
                    p.id AS ${SqlResult::id.name},
                    p.name AS ${SqlResult::name.name},
                    p.image_url AS ${SqlResult::imageUrl.name},
                    p.price AS ${SqlResult::price.name},
                    s.quantity AS ${SqlResult::quantity.name},
                    COUNT(*) OVER() AS ${SqlResult::totalCount.name}
                FROM product p INNER JOIN stock s
                ON p.id = s.product_id
                WHERE p.name LIKE :likeName
                ORDER BY ${query.sortCondition.property.propertyName} ${query.sortCondition.direction.name}
                LIMIT :offset, :pageSize
                """
        val nativeQuery =
            entityManager.createNativeQuery(query2, SqlResult::class.java)
                .setParameter("likeName", "%${query.likeProductName}%")
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
    ): SearchProductsResult {
        val totalCount =
            if (results.isNotEmpty()) {
                results.first().totalCount
            } else {
                0
            }

        return SearchProductsResult(
            page =
                Page(
                    items =
                        results.map {
                            SearchProductsResult.Summary(
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

    private data class SqlResult(
        val id: String,
        val name: String,
        val imageUrl: String?,
        val price: Int,
        val quantity: Int,
        val totalCount: Long,
    )
}
