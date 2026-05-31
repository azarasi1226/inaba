package jp.inaba.service2.feature.query.product.findbyid

import jp.inaba.message.product.query.FindProductByIdData
import jp.inaba.message.product.query.FindProductByIdQuery
import jp.inaba.message.product.query.FindProductByIdResult
import jp.inaba.service.infrastructure.jooq.generated.tables.references.PRODUCTS
import org.axonframework.messaging.queryhandling.annotation.QueryHandler
import org.jooq.DSLContext
import org.springframework.stereotype.Component

@Component
class FindProductByIdQueryHandler(
    private val dsl: DSLContext,
) {
    @QueryHandler
    fun handle(query: FindProductByIdQuery): FindProductByIdResult {
        val data = dsl
            .selectFrom(PRODUCTS)
            .where(PRODUCTS.ID.eq(query.id.value))
            .fetchOne {
                FindProductByIdData(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    imageUrl = it.imageUrl,
                    price = it.price,
                    quantity = it.quantity,
                )
            }

        return if (data != null) {
            FindProductByIdResult.success(data)
        } else {
            FindProductByIdResult.notFound()
        }
    }
}
