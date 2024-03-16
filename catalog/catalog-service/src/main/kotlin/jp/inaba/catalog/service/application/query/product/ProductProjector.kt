package jp.inaba.catalog.service.application.query.product

import jp.inaba.catalog.api.domain.product.ProductCreatedEvent
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
class ProductProjector {
    @EventHandler
    fun on(event: ProductCreatedEvent) {
        println("商品マスタテーブルに、一見追加しました${event}")
    }
}