package jp.inaba.service2.features.query.brand.search

import jp.inaba.core.domain.common.Page
import jp.inaba.core.domain.common.Paging
import jp.inaba.message.brand.query.SearchBrandsQuery
import jp.inaba.message.brand.query.SearchBrandsResult
import jp.inaba.message.brand.query.SearchBrandsSummary
import jp.inaba.service.infrastructure.jooq.generated.tables.references.BRANDS
import org.axonframework.messaging.queryhandling.annotation.QueryHandler
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Component

@Component
class SearchBrandQueryHandler(
    private val dsl: DSLContext,
) {
    @QueryHandler
    fun handle(query: SearchBrandsQuery): SearchBrandsResult {
        val totalCountField = DSL.count().over()
        val records = dsl
            .select(
                BRANDS.asterisk(),
                totalCountField,
            )
            .from(BRANDS)
            .where(BRANDS.NAME.like("%${query.likeBrandName}%"))
            .limit(query.pagingCondition.pageSize)
            .offset(query.pagingCondition.offset)
            .fetch()

        val totalCount = records.firstOrNull()?.get(totalCountField)?.toLong() ?: 0L

        return SearchBrandsResult.success(
            Page(
                items = records.map {
                    val brand = it.into(BRANDS)
                    SearchBrandsSummary(
                        id = brand.id,
                        name = brand.name,
                    )
                },
                paging = Paging(
                    totalCount = totalCount,
                    pageSize = query.pagingCondition.pageSize,
                    pageNumber = query.pagingCondition.pageNumber,
                ),
            ),
        )
    }
}
