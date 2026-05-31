package jp.inaba.service2.feature.command.product.update

import jp.inaba.core.domain.product.ProductId
import jp.inaba.message.CommandResult
import jp.inaba.message.InabaEventTag
import jp.inaba.message.product.command.UpdateProductCommand
import jp.inaba.message.product.command.UpdateProductResult
import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.message.product.event.ProductDeletedEvent
import jp.inaba.message.product.event.ProductUpdatedEvent
import org.axonframework.eventsourcing.annotation.EventSourcingHandler
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator
import org.axonframework.extension.spring.stereotype.EventSourced
import org.axonframework.messaging.commandhandling.annotation.CommandHandler
import org.axonframework.messaging.eventhandling.gateway.EventAppender
import org.axonframework.modelling.annotation.InjectEntity
import org.springframework.stereotype.Component

@Component
class UpdateProductCommandHandler {
    @CommandHandler
    fun handle(
        command: UpdateProductCommand,
        @InjectEntity state: State,
        eventAppender: EventAppender,
    ): CommandResult {
        if (!state.created) {
            return UpdateProductResult.notFound()
        }
        if (state.deleted) {
            return UpdateProductResult.deleted()
        }

        eventAppender.append(
            ProductUpdatedEvent(
                id = command.id.value,
                name = command.name.value,
                description = command.description.value,
                imageUrl = command.imageUrl?.value,
                price = command.price.value,
            ),
        )

        return UpdateProductResult.success()
    }

    @EventSourced(tagKey = InabaEventTag.PRODUCT_ID, idType = ProductId::class)
    class State(
        var created: Boolean,
        var deleted: Boolean,
    ) {
        @EntityCreator
        constructor() : this(
            created = false,
            deleted = false,
        )

        @EventSourcingHandler
        fun evolve(event: ProductCreatedEvent) {
            created = true
        }

        @EventSourcingHandler
        fun evolve(event: ProductDeletedEvent) {
            deleted = true
        }
    }
}
