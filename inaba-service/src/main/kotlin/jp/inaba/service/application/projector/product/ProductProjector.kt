package jp.inaba.service.application.projector.product

import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.message.product.event.ProductDeletedEvent
import jp.inaba.message.product.event.ProductUpdatedEvent
import jp.inaba.message.product.event.StockDecreasedEvent
import jp.inaba.message.product.event.StockIncreasedEvent
import jp.inaba.service.infrastructure.jooq.generated.tables.references.PRODUCTS
import jp.inaba.service.utlis.toTokyoLocalDateTime
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
import org.axonframework.eventhandling.Timestamp
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import java.time.Instant

@Component
@ProcessingGroup(ProductProjectorEventProcessor.PROCESSOR_NAME)
class ProductProjector(
    private val dsl: DSLContext,
) {
    @ResetHandler
    fun reset() {
        dsl.deleteFrom(PRODUCTS).execute()
    }

    @EventHandler
    fun on(
        event: ProductCreatedEvent,
        @Timestamp timestamp: Instant,
    ) {
        dsl
            .insertInto(
                PRODUCTS,
                PRODUCTS.ID,
                PRODUCTS.BRAND_ID,
                PRODUCTS.NAME,
                PRODUCTS.DESCRIPTION,
                PRODUCTS.IMAGE_URL,
                PRODUCTS.PRICE,
                PRODUCTS.QUANTITY,
                PRODUCTS.CREATED_AT,
                PRODUCTS.UPDATED_AT,
            ).values(
                event.id,
                event.brandId,
                event.name,
                event.description,
                event.imageUrl,
                event.price,
                event.quantity,
                timestamp.toTokyoLocalDateTime(),
                timestamp.toTokyoLocalDateTime(),
            )
            // 冪等性の考慮
            .onDuplicateKeyIgnore()
            .execute()
    }

    @EventHandler
    fun on(
        event: ProductUpdatedEvent,
        @Timestamp timestamp: Instant,
    ) {
        dsl
            .update(PRODUCTS)
            .set(PRODUCTS.NAME, event.name)
            .set(PRODUCTS.DESCRIPTION, event.description)
            .set(PRODUCTS.IMAGE_URL, event.imageUrl)
            .set(PRODUCTS.PRICE, event.price)
            .set(PRODUCTS.UPDATED_AT, timestamp.toTokyoLocalDateTime())
            .where(PRODUCTS.ID.eq(event.id))
            .execute()
    }

    @EventHandler
    fun on(
        event: StockIncreasedEvent,
        @Timestamp timestamp: Instant,
    ) {
        dsl
            .update(PRODUCTS)
            .set(PRODUCTS.QUANTITY, event.increasedStockQuantity)
            .set(PRODUCTS.UPDATED_AT, timestamp.toTokyoLocalDateTime())
            .where(PRODUCTS.ID.eq(event.id))
            .execute()
    }

    @EventHandler
    fun on(
        event: StockDecreasedEvent,
        @Timestamp timestamp: Instant,
    ) {
        dsl
            .update(PRODUCTS)
            .set(PRODUCTS.QUANTITY, event.decreasedStockQuantity)
            .set(PRODUCTS.UPDATED_AT, timestamp.toTokyoLocalDateTime())
            .where(PRODUCTS.ID.eq(event.id))
            .execute()
    }

    @EventHandler
    fun on(event: ProductDeletedEvent) {
        dsl.deleteFrom(PRODUCTS).where(PRODUCTS.ID.eq(event.id)).execute()
    }
}
