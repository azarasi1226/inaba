package jp.inaba.service2.feature.query.basket.findbyid

import jp.inaba.core.domain.common.Page
import jp.inaba.core.domain.common.Paging
import jp.inaba.message.basket.query.BasketItem
import jp.inaba.message.basket.query.FindBasketByIdData
import jp.inaba.message.basket.query.FindBasketByIdQuery
import jp.inaba.message.basket.query.FindBasketByIdResult
import jp.inaba.service.infrastructure.jooq.generated.tables.references.BASKET_ITEMS
import jp.inaba.service.infrastructure.jooq.generated.tables.references.PRODUCTS
import org.axonframework.messaging.queryhandling.annotation.QueryHandler
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Component

@Component
class FindBasketByIdQueryHandler(
    private val dsl: DSLContext,
) {
    @QueryHandler
    fun handle(query: FindBasketByIdQuery): FindBasketByIdResult {
        val totalCountField = DSL.count().over()
        val records = dsl
            .select(
                PRODUCTS.asterisk(),
                BASKET_ITEMS.asterisk(),
                totalCountField,
            )
            .from(BASKET_ITEMS)
            .join(PRODUCTS)
            .on(PRODUCTS.ID.eq(BASKET_ITEMS.PRODUCT_ID))
            .where(BASKET_ITEMS.BASKET_ID.eq(query.basketId.value))
            .orderBy(BASKET_ITEMS.ADDED_AT.asc())
            .limit(query.pagingCondition.pageSize)
            .offset(query.pagingCondition.offset)
            .fetch()

        val totalCount = records.firstOrNull()?.get(totalCountField)?.toLong() ?: 0L

        return FindBasketByIdResult.success(
            FindBasketByIdData(
                page = Page(
                    items = records.map {
                        val product = it.into(PRODUCTS)
                        val basket = it.into(BASKET_ITEMS)
                        BasketItem(
                            productId = product.id,
                            productName = product.name,
                            productPrice = product.price,
                            productImageUrl = product.imageUrl,
                            basketItemQuantity = basket.itemQuantity,
                        )
                    },
                    paging = Paging(
                        totalCount = totalCount,
                        pageSize = query.pagingCondition.pageSize,
                        pageNumber = query.pagingCondition.pageNumber,
                    ),
                ),
            ),
        )
    }
}
