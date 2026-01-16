package jp.inaba.service.application.projector.basket

import jp.inaba.message.basket.event.BasketClearedEvent
import jp.inaba.message.basket.event.BasketItemDeletedEvent
import jp.inaba.message.basket.event.BasketItemSetEvent
import jp.inaba.message.product.event.ProductDeletedEvent
import jp.inaba.service.infrastructure.jooq.generated.tables.records.BasketRecord
import jp.inaba.service.infrastructure.jooq.generated.tables.references.BASKET
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
        dsl.deleteFrom(BASKET).execute()
    }

    @EventHandler
    fun on(
        event: BasketItemSetEvent,
        @Timestamp timestamp: Instant,
    ) {
        val addedAt = timestamp.toTokyoLocalDateTime()
        val record =
            BasketRecord(
                basketId = event.id,
                productId = event.productId,
                itemQuantity = event.basketItemQuantity,
                addedAt = addedAt,
            )

        dsl
            .insertInto(BASKET)
            .set(record)
            .onDuplicateKeyUpdate()
            .set(BASKET.ITEM_QUANTITY, event.basketItemQuantity)
            .set(BASKET.ADDED_AT, addedAt)
            .execute()
    }

    @EventHandler
    fun on(event: BasketItemDeletedEvent) {
        dsl
            .deleteFrom(BASKET)
            .where(BASKET.BASKET_ID.eq(event.id).and(BASKET.PRODUCT_ID.eq(event.productId)))
            .execute()
    }

    @EventHandler
    fun on(event: BasketClearedEvent) {
        dsl
            .deleteFrom(BASKET)
            .where(BASKET.BASKET_ID.eq(event.id))
            .execute()
    }

    @EventHandler
    fun on(event: ProductDeletedEvent) {
        dsl
            .deleteFrom(BASKET)
            .where(BASKET.PRODUCT_ID.eq(event.id))
            .execute()
    }
}
