package jp.inaba.service.application.query.basket

import jp.inaba.core.domain.basket.FindBasketByIdError
import jp.inaba.core.domain.common.Page
import jp.inaba.core.domain.common.Paging
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.message.basket.query.FindBasketByIdQuery
import jp.inaba.message.basket.query.FindBasketByIdResult
import jp.inaba.service.infrastructure.jooq.generated.tables.references.BASKET
import jp.inaba.service.infrastructure.jooq.generated.tables.references.LOOKUP_BASKET
import jp.inaba.service.infrastructure.jooq.generated.tables.references.PRODUCT
import org.axonframework.queryhandling.QueryHandler
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Component

@Component
class FindBasketByIdQueryService(
    private val dsl: DSLContext,
) {
    @QueryHandler
    fun handle(query: FindBasketByIdQuery): FindBasketByIdResult {
        // 通常のBasketTableの場合、買い物かごにまだ商品が入っていなかったらテーブルデータがないため、存在しないと判断されてしまう。
        // そのため、買い物かごの存在確認用には、LookupBasketTableを使用する。
        val existsBasket =
            dsl.fetchExists(
                LOOKUP_BASKET,
                LOOKUP_BASKET.ID.eq(query.basketId.value),
            )
        if (!existsBasket) {
            throw UseCaseException(FindBasketByIdError.BASKET_NOT_FOUND)
        }

        val totalCountFiled = DSL.count().over()
        val records =
            dsl
                .select(
                    PRODUCT.asterisk(),
                    BASKET.asterisk(),
                    totalCountFiled,
                ).from(PRODUCT)
                .join(BASKET)
                .on(
                    PRODUCT.ID
                        .eq(BASKET.PRODUCT_ID)
                        // whereではなく join の on に basketId 条件を入れることで、join時の絞り込みを行い、高速なSQLを生成する。
                        .and(BASKET.BASKET_ID.eq(query.basketId.value)),
                ).orderBy(BASKET.ADDED_AT.asc())
                .limit(query.pagingCondition.pageSize)
                .offset(query.pagingCondition.offset)
                .fetch()

        val totalCount: Long = records.firstOrNull()?.get(totalCountFiled)?.toLong() ?: 0L
        return FindBasketByIdResult(
            page =
                Page(
                    items =
                        records.map {
                            val product = it.into(PRODUCT)
                            val basket = it.into(BASKET)
                            FindBasketByIdResult.BasketItem(
                                productId = product.id,
                                productName = product.name!!,
                                productPrice = product.price,
                                productImageUrl = product.imageUrl,
                                basketItemQuantity = basket.itemQuantity,
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
