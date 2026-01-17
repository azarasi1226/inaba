package jp.inaba.service.application.projector.basket

import jp.inaba.message.basket.event.BasketClearedEvent
import jp.inaba.message.basket.event.BasketItemDeletedEvent
import jp.inaba.message.basket.event.BasketItemSetEvent
import jp.inaba.message.product.event.ProductDeletedEvent
import jp.inaba.service.infrastructure.jooq.generated.tables.records.BasketItemsRecord
import jp.inaba.service.infrastructure.jooq.generated.tables.references.BASKET_ITEMS
import jp.inaba.service.utlis.toTokyoLocalDateTime
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
import org.axonframework.eventhandling.Timestamp
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import java.time.Instant

@Component
@ProcessingGroup(BasketProjectorEventProcessor.PROCESSOR_NAME)
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
                basketId = event.id,
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
            .where(BASKET_ITEMS.BASKET_ID.eq(event.id).and(BASKET_ITEMS.PRODUCT_ID.eq(event.productId)))
            .execute()
    }

    @EventHandler
    fun on(event: BasketClearedEvent) {
        dsl
            .deleteFrom(BASKET_ITEMS)
            .where(BASKET_ITEMS.BASKET_ID.eq(event.id))
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
