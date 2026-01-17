package jp.inaba.service.application.projector.brand

import jp.inaba.message.brand.event.BrandCreatedEvent
import jp.inaba.message.brand.event.BrandDeletedEvent
import jp.inaba.service.infrastructure.jooq.generated.tables.references.BRAND
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
        dsl.deleteFrom(BRAND).execute()
    }

    @EventHandler
    fun on(
        event: BrandCreatedEvent,
        @Timestamp timestamp: Instant,
    ) {
        dsl
            .insertInto(
                BRAND,
                BRAND.ID,
                BRAND.NAME,
                BRAND.CREATED_AT,
                BRAND.UPDATED_AT,
            ).values(
                event.id,
                event.name,
                timestamp.toTokyoLocalDateTime(),
                timestamp.toTokyoLocalDateTime(),
            ).execute()
    }

    @EventHandler
    fun on(event: BrandDeletedEvent) {
        dsl.deleteFrom(BRAND).where(BRAND.ID.eq(event.id)).execute()
    }
}
