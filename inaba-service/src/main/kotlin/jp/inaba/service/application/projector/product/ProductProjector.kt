package jp.inaba.service.application.projector.product

import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.message.product.event.ProductDeletedEvent
import jp.inaba.message.product.event.ProductUpdatedEvent
import jp.inaba.message.product.event.StockDecreasedEvent
import jp.inaba.message.product.event.StockIncreasedEvent
import jp.inaba.service.infrastructure.jooq.generated.tables.references.PRODUCT
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
        dsl.deleteFrom(PRODUCT).execute()
    }

    @EventHandler
    fun on(
        event: ProductCreatedEvent,
        @Timestamp timestamp: Instant,
    ) {
        dsl
            .insertInto(
                PRODUCT,
                PRODUCT.ID,
                PRODUCT.BRAND_ID,
                PRODUCT.NAME,
                PRODUCT.DESCRIPTION,
                PRODUCT.IMAGE_URL,
                PRODUCT.PRICE,
                PRODUCT.QUANTITY,
                PRODUCT.CREATED_AT,
                PRODUCT.UPDATED_AT,
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
            .update(PRODUCT)
            .set(PRODUCT.NAME, event.name)
            .set(PRODUCT.DESCRIPTION, event.description)
            .set(PRODUCT.IMAGE_URL, event.imageUrl)
            .set(PRODUCT.PRICE, event.price)
            .set(PRODUCT.UPDATED_AT, timestamp.toTokyoLocalDateTime())
            .where(PRODUCT.ID.eq(event.id))
            .execute()
    }

    @EventHandler
    fun on(
        event: StockIncreasedEvent,
        @Timestamp timestamp: Instant,
    ) {
        dsl
            .update(PRODUCT)
            .set(PRODUCT.QUANTITY, event.increasedStockQuantity)
            .set(PRODUCT.UPDATED_AT, timestamp.toTokyoLocalDateTime())
            .where(PRODUCT.ID.eq(event.id))
            .execute()
    }

    @EventHandler
    fun on(
        event: StockDecreasedEvent,
        @Timestamp timestamp: Instant,
    ) {
        dsl
            .update(PRODUCT)
            .set(PRODUCT.QUANTITY, event.decreasedStockQuantity)
            .set(PRODUCT.UPDATED_AT, timestamp.toTokyoLocalDateTime())
            .where(PRODUCT.ID.eq(event.id))
            .execute()
    }

    @EventHandler
    fun on(event: ProductDeletedEvent) {
        dsl.deleteFrom(PRODUCT).where(PRODUCT.ID.eq(event.id)).execute()
    }
}
