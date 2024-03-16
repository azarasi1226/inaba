package jp.inaba.catalog.service.application.query.product

import jp.inaba.catalog.api.domain.product.ProductQueries
import jp.inaba.catalog.service.infrastructure.jpa.product.ProductJpaRepository
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class ProductQueryService(
    private val projectJpaRepository: ProductJpaRepository
) {
    @QueryHandler
    fun handle(query: ProductQueries.FindById): ProductQueries.FindByIdResult {
        val entity = projectJpaRepository.findById(query.id)
            .orElseThrow { Exception("存在しない") }

        return ProductQueries.FindByIdResult(
            id = entity.productId,
            name = entity.productName,
            description = entity.description,
            imageUrl = entity.imageUrl,
            price = entity.price,
            quantity = entity.quantity
        )
    }
}