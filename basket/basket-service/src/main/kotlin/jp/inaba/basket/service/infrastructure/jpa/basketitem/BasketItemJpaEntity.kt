package jp.inaba.basket.service.infrastructure.jpa.basketitem

import jakarta.persistence.*
import jp.inaba.basket.service.infrastructure.jpa.basket.BasketJpaEntity
import jp.inaba.basket.service.infrastructure.jpa.product.ProductJpaEntity

@Entity
@Table(name = "basket_item")
data class BasketItemJpaEntity(
    @EmbeddedId
    var basketItemId: BasketItemId = BasketItemId(),
    @ManyToOne
    @JoinColumn(name = "basket_id", referencedColumnName = "id", insertable = false, updatable = false)
    var basket: BasketJpaEntity = BasketJpaEntity(),
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    var product: ProductJpaEntity = ProductJpaEntity(),
    var itemQuantity: Int = 0
)

@Embeddable
data class BasketItemId(
    @Column(name = "basket_id")
    var basketId: String = "",
    @Column(name = "product_id")
    var productId: String = ""
)