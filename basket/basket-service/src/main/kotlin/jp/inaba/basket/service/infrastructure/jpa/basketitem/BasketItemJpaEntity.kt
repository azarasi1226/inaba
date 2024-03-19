package jp.inaba.basket.service.infrastructure.jpa.basketitem

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jp.inaba.basket.service.infrastructure.jpa.basket.BasketJpaEntity
import jp.inaba.basket.service.infrastructure.jpa.product.ProductJpaEntity

@Entity
data class BasketItemJpaEntity(
    @Id
    var basketItemId: String = "",
    @ManyToOne
    @JoinColumn(name = "basket_id")
    var basket: BasketJpaEntity = BasketJpaEntity(),
    @ManyToOne
    @JoinColumn(name = "item_id")
    var product: ProductJpaEntity = ProductJpaEntity(),
    var itemQuantity: Int = 0
)