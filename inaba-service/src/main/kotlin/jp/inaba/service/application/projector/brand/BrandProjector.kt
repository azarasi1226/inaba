package jp.inaba.service.application.projector.brand

import jp.inaba.message.brand.event.BrandCreatedEvent
import jp.inaba.message.brand.event.BrandDeletedEvent
import jp.inaba.service.infrastructure.jooq.generated.tables.references.BRANDS
import jp.inaba.service.utlis.toTokyoLocalDateTime
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
import org.axonframework.eventhandling.Timestamp
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import java.time.Instant

@Component
@ProcessingGroup(BrandProjectorEventProcessor.PROCESSOR_NAME)
class BrandProjector(
    private val dsl: DSLContext,
) {
    @ResetHandler
    fun reset() {
        dsl.deleteFrom(BRANDS).execute()
    }

    @EventHandler
    fun on(
        event: BrandCreatedEvent,
        @Timestamp timestamp: Instant,
    ) {
        dsl
            .insertInto(
                BRANDS,
                BRANDS.ID,
                BRANDS.NAME,
                BRANDS.CREATED_AT,
                BRANDS.UPDATED_AT,
            ).values(
                event.id,
                event.name,
                timestamp.toTokyoLocalDateTime(),
                timestamp.toTokyoLocalDateTime(),
            )
            // 冪等性の考慮
            .onDuplicateKeyIgnore()
            .execute()
    }

    @EventHandler
    fun on(event: BrandDeletedEvent) {
        dsl.deleteFrom(BRANDS).where(BRANDS.ID.eq(event.id)).execute()
    }
}
