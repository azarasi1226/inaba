package jp.inaba.service.infrastructure.domain.basket

import jp.inaba.core.domain.basket.BasketItemQuantity
import jp.inaba.core.domain.product.ProductId
import jp.inaba.service.domain.basket.SetBasketItemVerifier
import jp.inaba.service.infrastructure.jooq.generated.tables.references.PRODUCTS
import org.jooq.DSLContext
import org.springframework.stereotype.Service

@Service
class SetBasketItemVerifierImpl(
    private val dsl: DSLContext,
) : SetBasketItemVerifier {
    override fun isProductNotFound(productId: ProductId): Boolean =
        !dsl.fetchExists(
            dsl.selectOne().from(PRODUCTS).where(PRODUCTS.ID.eq(productId.value)),
        )

    override fun isOutOfStock(
        productId: ProductId,
        basketItemQuantity: BasketItemQuantity,
    ): Boolean {
        val quantity: Int =
            dsl
                .select(PRODUCTS.QUANTITY)
                .from(PRODUCTS)
                .where(PRODUCTS.ID.eq(productId.value))
                .fetchOne(PRODUCTS.QUANTITY) ?: return true

        return quantity < basketItemQuantity.value
    }
}
