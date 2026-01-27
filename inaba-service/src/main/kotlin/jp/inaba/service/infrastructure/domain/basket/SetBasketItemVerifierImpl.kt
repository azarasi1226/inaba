package jp.inaba.service.infrastructure.domain.basket

import jp.inaba.core.domain.basket.BasketItemQuantity
import jp.inaba.core.domain.product.ProductId
import jp.inaba.service.domain.basket.SetBasketItemVerifier
import jp.inaba.service.domain.product.ProductAggregate
import org.axonframework.modelling.command.Repository
import org.jooq.DSLContext
import org.springframework.stereotype.Service

@Service
class SetBasketItemVerifierImpl(
    private val dsl: DSLContext,
    private val productRepository: Repository<ProductAggregate>,
) : SetBasketItemVerifier {
    override fun isProductNotFound(productId: ProductId): Boolean =
        try {
            productRepository.load(productId.value)
            false
        } catch (_: Exception) {
            true
        }

    override fun isOutOfStock(
        productId: ProductId,
        basketItemQuantity: BasketItemQuantity,
    ): Boolean =
        try {
            val product = productRepository.load(productId.value)
            val stock = product.invoke { it.stock.quantity }

            stock.value < basketItemQuantity.value
        } catch (_: Exception) {
            true
        }
}
