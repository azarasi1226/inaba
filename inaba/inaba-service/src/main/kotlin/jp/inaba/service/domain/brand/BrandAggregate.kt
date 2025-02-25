package jp.inaba.service.domain.brand

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.message.brand.command.DeleteBrandCommand
import jp.inaba.message.brand.event.BrandCreatedEvent
import jp.inaba.message.brand.event.BrandDeletedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class BrandAggregate() {
    @AggregateIdentifier
    private lateinit var id: BrandId

    @CommandHandler
    constructor(command: InternalCreateBrandCommand) : this() {
        val event =
            BrandCreatedEvent(
                id = command.id.value,
                name = command.name.value,
            )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: DeleteBrandCommand) {
        val event = BrandDeletedEvent(command.id.value)

        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: BrandCreatedEvent) {
        id = BrandId(event.id)
    }

    @EventSourcingHandler
    fun on(event: BrandDeletedEvent) {
        AggregateLifecycle.markDeleted()
    }
}
