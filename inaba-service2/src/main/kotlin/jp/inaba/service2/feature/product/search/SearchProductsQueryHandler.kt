package jp.inaba.service2.feature.query.product.search

import jp.inaba.core.domain.common.Page
import jp.inaba.core.domain.common.Paging
import jp.inaba.message.product.query.SearchProductsQuery
import jp.inaba.message.product.query.SearchProductsResult
import jp.inaba.message.product.query.SearchProductsSummary
import jp.inaba.service.infrastructure.jooq.generated.tables.references.PRODUCTS
import jp.inaba.service2.utlis.toOrderField
import org.axonframework.messaging.queryhandling.annotation.QueryHandler
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Component

@Component
class SearchProductsQueryHandler(
    private val dsl: DSLContext,
) {
    @QueryHandler
    fun handle(query: SearchProductsQuery): SearchProductsResult {
        val totalCountField = DSL.count().over()
        val records = dsl
            .select(
                PRODUCTS.asterisk(),
                totalCountField,
            )
            .from(PRODUCTS)
            .where(PRODUCTS.NAME.like("%${query.likeProductName}%"))
            .orderBy(query.sortCondition.toOrderField())
            .limit(query.pagingCondition.pageSize)
            .offset(query.pagingCondition.offset)
            .fetch()

        val totalCount = records.firstOrNull()?.get(totalCountField)?.toLong() ?: 0L

        return SearchProductsResult.success(
            Page(
                items = records.map {
                    val product = it.into(PRODUCTS)
                    SearchProductsSummary(
                        id = product.id,
                        name = product.name,
                        imageUrl = product.imageUrl,
                        price = product.price,
                        quantity = product.quantity,
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
