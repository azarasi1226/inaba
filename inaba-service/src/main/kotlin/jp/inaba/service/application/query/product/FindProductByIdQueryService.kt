package jp.inaba.service.application.query.product

import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.core.domain.product.FindProductByIdError
import jp.inaba.message.product.query.FindProductByIdQuery
import jp.inaba.message.product.query.FindProductByIdResult
import jp.inaba.service.infrastructure.jooq.generated.tables.references.PRODUCTS
import org.axonframework.queryhandling.QueryHandler
import org.jooq.DSLContext
import org.springframework.stereotype.Component

@Component
class FindProductByIdQueryService(
    private val dsl: DSLContext,
) {
    @QueryHandler
    fun handle(query: FindProductByIdQuery): FindProductByIdResult =
        dsl
            .selectFrom(PRODUCTS)
            .where(PRODUCTS.ID.eq(query.id.value))
            .fetchOne {
                FindProductByIdResult(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    imageUrl = it.imageUrl,
                    price = it.price,
                    quantity = it.quantity,
                )
            } ?: throw UseCaseException(FindProductByIdError.PRODUCT_NOT_FOUND)
}
