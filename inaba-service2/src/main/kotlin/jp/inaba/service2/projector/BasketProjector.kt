package jp.inaba.service2.projector

import jp.inaba.message.basket.event.BasketClearedEvent
import jp.inaba.message.basket.event.BasketItemDeletedEvent
import jp.inaba.message.basket.event.BasketItemSetEvent
import jp.inaba.message.product.event.ProductDeletedEvent
import jp.inaba.service.infrastructure.jooq.generated.tables.records.BasketItemsRecord
import jp.inaba.service.infrastructure.jooq.generated.tables.references.BASKET_ITEMS
import jp.inaba.service2.utlis.toTokyoLocalDateTime
import org.axonframework.messaging.eventhandling.annotation.EventHandler
import org.axonframework.messaging.eventhandling.annotation.Timestamp
import org.axonframework.messaging.eventhandling.replay.annotation.ResetHandler
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class BasketProjector(
    private val dsl: DSLContext,
) {
    @ResetHandler
    fun reset() {
        dsl.deleteFrom(BASKET_ITEMS).execute()
    }

    @EventHandler
    fun on(
        event: BasketItemSetEvent,
        @Timestamp timestamp: Instant,
    ) {
        val addedAt = timestamp.toTokyoLocalDateTime()
        val record =
            BasketItemsRecord(
                basketId = event.userId,
                productId = event.productId,
                itemQuantity = event.basketItemQuantity,
                addedAt = addedAt,
            )

        dsl
            .insertInto(BASKET_ITEMS)
            .set(record)
            .onDuplicateKeyUpdate()
            .set(BASKET_ITEMS.ITEM_QUANTITY, event.basketItemQuantity)
            .set(BASKET_ITEMS.ADDED_AT, addedAt)
            .execute()
    }

    @EventHandler
    fun on(event: BasketItemDeletedEvent) {
        dsl
            .deleteFrom(BASKET_ITEMS)
            .where(BASKET_ITEMS.BASKET_ID.eq(event.userId).and(BASKET_ITEMS.PRODUCT_ID.eq(event.productId)))
            .execute()
    }

    @EventHandler
    fun on(event: BasketClearedEvent) {
        dsl
            .deleteFrom(BASKET_ITEMS)
            .where(BASKET_ITEMS.BASKET_ID.eq(event.userId))
            .execute()
    }

    @EventHandler
    fun on(event: ProductDeletedEvent) {
        dsl
            .deleteFrom(BASKET_ITEMS)
            .where(BASKET_ITEMS.PRODUCT_ID.eq(event.id))
            .execute()
    }
}
