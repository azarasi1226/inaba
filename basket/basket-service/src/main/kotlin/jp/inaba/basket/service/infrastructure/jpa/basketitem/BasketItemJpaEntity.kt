package jp.inaba.basket.service.infrastructure.jpa.basketitem

import jakarta.persistence.*
import jp.inaba.basket.service.infrastructure.jpa.basket.BasketJpaEntity
import jp.inaba.basket.service.infrastructure.jpa.product.ProductJpaEntity

@Entity
@Table(name = "basket_item")
data class BasketItemJpaEntity(
    //TODO(basketItemIdいらないよね...basketとproductの複合主キーにしないとだめ？それか、自動栽培のほうがいいのかな？)
    @Id
    var basketItemId: String = "",
    @ManyToOne
    @JoinColumn(name = "basket_id")
    var basket: BasketJpaEntity = BasketJpaEntity(),
    @ManyToOne
    @JoinColumn(name = "product_id")
    var product: ProductJpaEntity = ProductJpaEntity(),
    var itemQuantity: Int = 0
)