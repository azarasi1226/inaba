package jp.inaba.catalog.service.application.query.product

import jp.inaba.catalog.api.domain.product.query.FindProductByIdQuery
import jp.inaba.catalog.api.domain.product.query.FindProductByIdResult
import jp.inaba.catalog.service.infrastructure.jpa.product.ProductJpaRepository
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class ProductFindByIdQueryService(
    private val productJpaRepository: ProductJpaRepository,
) {
    @QueryHandler
    fun handle(query: FindProductByIdQuery): Optional<FindProductByIdResult> {
        val maybeEntity = productJpaRepository.findById(query.id.value)

        return if (maybeEntity.isEmpty) {
            Optional.empty()
        } else {
            val entity = maybeEntity.get()
            Optional.of(
                FindProductByIdResult(
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
