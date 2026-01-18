package jp.inaba.service.application.query.brand

import jp.inaba.core.domain.common.Page
import jp.inaba.core.domain.common.Paging
import jp.inaba.message.brand.query.SearchBrandsQuery
import jp.inaba.message.brand.query.SearchBrandsResult
import jp.inaba.service.infrastructure.jooq.generated.tables.references.BRANDS
import org.axonframework.queryhandling.QueryHandler
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Component

@Component
class SearchBrandsQueryService(
    private val dsl: DSLContext,
) {
    @QueryHandler
    fun handle(query: SearchBrandsQuery): SearchBrandsResult {
        val totalCountFiled = DSL.count().over()
        val records =
            dsl
                .select(
                    BRANDS.asterisk(),
                    totalCountFiled,
                ).from(BRANDS)
                .where(BRANDS.NAME.like("%${query.likeBrandName}%"))
                .limit(query.pagingCondition.pageSize)
                .offset(query.pagingCondition.offset)
                .fetch()

        val totalCount: Long = records.firstOrNull()?.get(totalCountFiled)?.toLong() ?: 0L
        return SearchBrandsResult(
            page =
                Page(
                    items =
                        records.map {
                            val brand = it.into(BRANDS)
                            SearchBrandsResult.Summary(
                                id = brand.id,
                                name = brand.name,
                            )
                        },
                    paging =
                        Paging(
                            totalCount = totalCount,
                            pageSize = query.pagingCondition.pageSize,
                            pageNumber = query.pagingCondition.pageNumber,
                        ),
                ),
        )
    }
}
