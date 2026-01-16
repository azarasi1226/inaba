package jp.inaba.service.application.query.product

import jp.inaba.core.domain.common.Page
import jp.inaba.core.domain.common.Paging
import jp.inaba.message.product.query.SearchProductsQuery
import jp.inaba.message.product.query.SearchProductsResult
import jp.inaba.service.infrastructure.jooq.generated.tables.references.PRODUCT
import jp.inaba.service.utlis.toOrderField
import org.axonframework.queryhandling.QueryHandler
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Component

@Component
class SearchProductsQueryService(
    private val dsl: DSLContext,
) {
    @QueryHandler
    fun handle(query: SearchProductsQuery): SearchProductsResult {
        val totalCountFiled = DSL.count().over()
        val records =
            dsl
                .select(
                    PRODUCT.asterisk(),
                    totalCountFiled,
                ).from(PRODUCT)
                .where(PRODUCT.NAME.like("%${query.likeProductName}%"))
                .orderBy(query.sortCondition.toOrderField())
                .limit(query.pagingCondition.pageSize)
                .offset(query.pagingCondition.offset)
                .fetch()

        val totalCount: Long = records.firstOrNull()?.get(totalCountFiled)?.toLong() ?: 0L

        return SearchProductsResult(
            page =
                Page(
                    items =
                        records.map {
                            val product = it.into(PRODUCT)
                            SearchProductsResult.Summary(
                                id = product.id,
                                name = product.name!!,
                                imageUrl = product.imageUrl,
                                price = product.price,
                                quantity = product.quantity,
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
