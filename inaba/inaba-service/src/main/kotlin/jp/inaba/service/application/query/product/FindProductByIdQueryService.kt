package jp.inaba.service.application.query.product

import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.core.domain.product.FindProductByIdError
import jp.inaba.message.product.query.FindProductByIdQuery
import jp.inaba.message.product.query.FindProductByIdResult
import jp.inaba.service.infrastructure.jpa.product.ProductJpaRepository
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class FindProductByIdQueryService(
    private val productJpaRepository: ProductJpaRepository,
) {
    @QueryHandler
    fun handle(query: FindProductByIdQuery): FindProductByIdResult {
        val maybeEntity = productJpaRepository.findById(query.id.value)

        return if (maybeEntity.isEmpty) {
            throw UseCaseException(FindProductByIdError.PRODUCT_NOT_FOUND)
        } else {
            val entity = maybeEntity.get()
                FindProductByIdResult(
                    id = entity.id,
                    //TODO (nullableだけど、ない場合とかあるか？後で考えよう。)
                    stockId = entity.stockId!!,
                    name = entity.name,
                    description = entity.description,
                    imageUrl = entity.imageUrl,
                    price = entity.price,
                    quantity = entity.quantity,
                )
        }
    }
}
