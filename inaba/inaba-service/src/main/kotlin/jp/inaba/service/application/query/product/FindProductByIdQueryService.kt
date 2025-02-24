package jp.inaba.service.application.query.product

import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.core.domain.product.FindProductByIdError
import jp.inaba.message.product.query.FindProductByIdQuery
import jp.inaba.message.product.query.FindProductByIdResult
import jp.inaba.service.infrastructure.jpa.product.ProductJpaRepository
import jp.inaba.service.infrastructure.jpa.stock.StockJpaRepository
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class FindProductByIdQueryService(
    private val productJpaRepository: ProductJpaRepository,
    private val stockJpaRepository: StockJpaRepository,
) {
    @QueryHandler
    fun handle(query: FindProductByIdQuery): FindProductByIdResult {
        val productJpaEntity =
            productJpaRepository.findById(query.id.value).orElseThrow {
                UseCaseException(FindProductByIdError.PRODUCT_NOT_FOUND)
            }
        val stockJpaEntity =
            stockJpaRepository.findByProductId(query.id.value).orElseThrow {
                UseCaseException(FindProductByIdError.PRODUCT_NOT_FOUND)
            }

        return FindProductByIdResult(
            id = productJpaEntity.id,
            name = productJpaEntity.name,
            description = productJpaEntity.description,
            imageUrl = productJpaEntity.imageUrl,
            price = productJpaEntity.price,
            stockId = stockJpaEntity.id,
            quantity = stockJpaEntity.quantity,
        )
    }
}
