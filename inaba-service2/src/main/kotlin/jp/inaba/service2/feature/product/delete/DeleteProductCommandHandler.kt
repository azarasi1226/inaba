package jp.inaba.service2.feature.command.product.delete

import jp.inaba.core.domain.product.ProductId
import jp.inaba.message.CommandResult
import jp.inaba.message.InabaEventTag
import jp.inaba.message.product.command.DeleteProductCommand
import jp.inaba.message.product.command.DeleteProductResult
import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.message.product.event.ProductDeletedEvent
import org.axonframework.eventsourcing.annotation.EventSourcingHandler
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator
import org.axonframework.extension.spring.stereotype.EventSourced
import org.axonframework.messaging.commandhandling.annotation.CommandHandler
import org.axonframework.messaging.eventhandling.gateway.EventAppender
import org.axonframework.modelling.annotation.InjectEntity
import org.springframework.stereotype.Component

@Component
class DeleteProductCommandHandler {
    @CommandHandler
    fun handle(
        command: DeleteProductCommand,
        @InjectEntity state: State,
        eventAppender: EventAppender,
    ): CommandResult {
        if (!state.created) {
            return DeleteProductResult.notFound()
        }
        // 冪等性を考慮し、すでに削除されている場合は成功を返す
        if (state.deleted) {
            return DeleteProductResult.success()
        }

        eventAppender.append(
            ProductDeletedEvent(
                id = command.id.value,
            ),
        )

        return DeleteProductResult.success()
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
