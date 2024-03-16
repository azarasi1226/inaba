package jp.inaba.catalog.service.application.query.product

import jp.inaba.catalog.api.domain.product.ProductQueries
import jp.inaba.catalog.service.infrastructure.jpa.product.ProductJpaRepository
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class ProductQueryService(
    private val productJpaRepository: ProductJpaRepository
) {
    @QueryHandler
    fun handle(query: ProductQueries.FindById): ProductQueries.FindByIdResult {
        val entity = productJpaRepository.findById(query.id)
            .orElseThrow { ProductNotFoundException(query.id) }

        return ProductQueries.FindByIdResult(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            imageUrl = entity.imageUrl,
            price = entity.price,
            quantity = entity.quantity
        )
    }
}