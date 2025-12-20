package jp.inaba.service.application.query.brand

import jakarta.persistence.EntityManager
import jakarta.persistence.Query
import jp.inaba.core.domain.common.Page
import jp.inaba.core.domain.common.Paging
import jp.inaba.core.domain.common.PagingCondition
import jp.inaba.message.brand.query.SearchBrandsQuery
import jp.inaba.message.brand.query.SearchBrandsResult
import jp.inaba.service.utlis.sqlDebugLogging
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class SearchBrandsQueryService(
    private val entityManager: EntityManager,
) {
    @QueryHandler
    fun handle(query: SearchBrandsQuery): SearchBrandsResult {
        val nativeQuery = createNativeQuery(query)

        val results = nativeQuery.resultList.filterIsInstance<SqlResult>()

        return convertToQueryResult(
            results = results,
            pagingCondition = query.pagingCondition,
        )
    }

    private fun createNativeQuery(query: SearchBrandsQuery): Query {
        val sql =
"""
SELECT
    b.id AS ${SqlResult::id.name},
    b.name AS ${SqlResult::name.name},
    COUNT(*) OVER() AS ${SqlResult::totalCount.name}
FROM brand b
WHERE b.name LIKE :likeName
LIMIT :offset, :pageSize
"""

        val nativeQuery =
            entityManager
                .createNativeQuery(sql, SqlResult::class.java)
                .setParameter("likeName", "%${query.likeBrandName}%")
                .setParameter("offset", query.pagingCondition.offset)
                .setParameter("pageSize", query.pagingCondition.pageSize)

        nativeQuery.sqlDebugLogging()

        return nativeQuery
    }

    private fun convertToQueryResult(
        results: List<SqlResult>,
        pagingCondition: PagingCondition,
    ): SearchBrandsResult {
        val totalCount =
            if (results.isNotEmpty()) {
                results.first().totalCount
            } else {
                0
            }

        return SearchBrandsResult(
            page =
                Page(
                    items =
                        results.map {
                            SearchBrandsResult.Summary(
                                id = it.id,
                                name = it.name,
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
        val totalCount: Long,
    )
}
