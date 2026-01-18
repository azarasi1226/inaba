package jp.inaba.service.application.projector.lookupbasket

import jp.inaba.message.basket.event.BasketCreatedEvent
import jp.inaba.message.basket.event.BasketDeletedEvent
import jp.inaba.service.infrastructure.jooq.generated.tables.references.LOOKUP_BASKETS
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.jooq.DSLContext
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(LookupBasketProjectorEventProcessor.PROCESSOR_NAME)
class LookupBasketProjector(
    private val dsl: DSLContext,
) {
    @EventHandler
    fun on(event: BasketCreatedEvent) {
        dsl
            .insertInto(
                LOOKUP_BASKETS,
                LOOKUP_BASKETS.ID,
                LOOKUP_BASKETS.USER_ID,
            ).values(
                event.id,
                event.userId,
            )
            // このテーブルは一意聖を保証するためのテーブルなので、重複したら例外とする
            .execute()
    }

    @EventHandler
    fun on(event: BasketDeletedEvent) {
        dsl.deleteFrom(LOOKUP_BASKETS).where(LOOKUP_BASKETS.ID.eq(event.id)).execute()
    }
}
