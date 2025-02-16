package jp.inaba.service.infrastructure.domain.basket

import jp.inaba.core.domain.basket.BasketItemQuantity
import jp.inaba.core.domain.product.ProductId
import jp.inaba.service.domain.basket.SetBasketItemVerifier
import jp.inaba.service.infrastructure.jpa.lookupproduct.LookupProductJpaRepository
import jp.inaba.service.infrastructure.jpa.stock.StockJpaRepository
import org.springframework.stereotype.Service

@Service
class SetBasketItemVerifierImpl(
    private val lookupProductJpaRepository: LookupProductJpaRepository,
    private val stockJpaRepository: StockJpaRepository,
) : SetBasketItemVerifier {
    override fun isProductNotFound(productId: ProductId): Boolean {
        return !lookupProductJpaRepository.existsById(productId.value)
    }

    override fun isOutOfStock(
        productId: ProductId,
        basketItemQuantity: BasketItemQuantity,
    ): Boolean {
        val entity = stockJpaRepository.findByProductId(productId.value).orElseThrow()

        return entity.quantity < basketItemQuantity.value
    }
}
