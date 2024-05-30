package jp.inaba.basket.service.infrastructure.jpa.basketitem

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jp.inaba.basket.service.infrastructure.jpa.product.ProductJpaEntity

@Entity
@Table(name = "basket_item")
data class BasketItemJpaEntity(
    @EmbeddedId
    var basketItemId: BasketItemId = BasketItemId(),
    @Column(name = "basket_id", insertable = false, updatable = false)
    var basketId: String = "",
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    var product: ProductJpaEntity = ProductJpaEntity(),
    var itemQuantity: Int = 0,
)

@Embeddable
data class BasketItemId(
    @Column(name = "basket_id")
    var basketId: String = "",
    @Column(name = "product_id")
    var productId: String = "",
)
