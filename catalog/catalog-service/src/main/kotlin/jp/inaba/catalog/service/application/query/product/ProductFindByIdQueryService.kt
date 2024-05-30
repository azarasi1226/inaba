package jp.inaba.catalog.service.application.query.product

import jp.inaba.catalog.api.domain.product.ProductQueries
import jp.inaba.catalog.service.infrastructure.jpa.product.ProductJpaRepository
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class ProductFindByIdQueryService(
    private val productJpaRepository: ProductJpaRepository,
) {
    @QueryHandler
    fun handle(query: ProductQueries.FindById): Optional<ProductQueries.FindByIdResult> {
        val maybeEntity = productJpaRepository.findById(query.id.value)

        return if (maybeEntity.isEmpty) {
            Optional.empty()
        } else {
            val entity = maybeEntity.get()
            Optional.of(
                ProductQueries.FindByIdResult(
                    id = entity.id,
                    name = entity.name,
                    description = entity.description,
                    imageUrl = entity.imageUrl,
                    price = entity.price,
                    quantity = entity.quantity,
                ),
            )
        }
    }
}
