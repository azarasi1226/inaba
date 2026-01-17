package jp.inaba.service.application.projector.order

import jp.inaba.message.order.event.OrderCompletedEvent
import jp.inaba.message.order.event.OrderFailedEvent
import jp.inaba.message.order.event.OrderIssuedEvent
import jp.inaba.service.domain.order.OrderStatus
import jp.inaba.service.infrastructure.jooq.generated.tables.references.ORDERS
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
import org.jooq.DSLContext
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(OrderProjectorEventProcessor.PROCESSOR_NAME)
class OrderProjector(
    private val dsl: DSLContext,
) {
    @ResetHandler
    fun reset() {
        dsl.deleteFrom(ORDERS).execute()
    }

    @EventHandler
    fun on(event: OrderIssuedEvent) {
        dsl
            .insertInto(
                ORDERS,
                ORDERS.ID,
                ORDERS.USER_ID,
                ORDERS.STATUS,
            ).values(event.id, event.userId, OrderStatus.Issued.value)
            .onDuplicateKeyIgnore()
            .execute()
    }

    @EventHandler
    fun on(event: OrderCompletedEvent) {
        dsl
            .update(ORDERS)
            .set(ORDERS.STATUS, OrderStatus.Completed.value)
            .where(ORDERS.ID.eq(event.id))
            .execute()
    }

    @EventHandler
    fun on(event: OrderFailedEvent) {
        dsl
            .update(ORDERS)
            .set(ORDERS.STATUS, OrderStatus.Failed.value)
            .where(ORDERS.ID.eq(event.id))
            .execute()
    }
}
