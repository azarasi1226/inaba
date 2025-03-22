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
        val entity =
            productJpaRepository.findById(query.id.value).orElseThrow {
                UseCaseException(FindProductByIdError.PRODUCT_NOT_FOUND)
            }

        return FindProductByIdResult(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            imageUrl = entity.imageUrl,
            price = entity.price,
            quantity = entity.quantity,
        )
    }
}
