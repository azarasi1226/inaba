package jp.inaba.service2.features.command.basket.deleteitem

import jp.inaba.core.domain.user.UserId
import jp.inaba.message.InabaEventTag
import jp.inaba.message.basket.command.DeleteBasketItemCommand
import jp.inaba.message.basket.command.DeleteBasketItemResult
import jp.inaba.message.basket.event.BasketClearedEvent
import jp.inaba.message.basket.event.BasketItemDeletedEvent
import jp.inaba.message.basket.event.BasketItemSetEvent
import org.axonframework.eventsourcing.annotation.EventSourcingHandler
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator
import org.axonframework.extension.spring.stereotype.EventSourced
import org.axonframework.messaging.commandhandling.annotation.CommandHandler
import org.axonframework.messaging.eventhandling.gateway.EventAppender
import org.axonframework.modelling.annotation.InjectEntity
import org.springframework.stereotype.Component

@Component
class DeleteBasketItemCommandHandler {
    @CommandHandler
    fun handle(
        command: DeleteBasketItemCommand,
        @InjectEntity state: State,
        eventAppender: EventAppender,
    ): DeleteBasketItemResult {
        if (command.productId.value !in state.itemProductIds) {
            return DeleteBasketItemResult.success()
        }

        eventAppender.append(
            BasketItemDeletedEvent(
                userId = command.id.value,
                productId = command.productId.value,
            ),
        )

        return DeleteBasketItemResult.success()
    }

    @EventSourced(tagKey = InabaEventTag.USER_ID, idType = UserId::class)
    class State(
        var itemProductIds: MutableSet<String>,
    ) {
        @EntityCreator
        constructor() : this(
            itemProductIds = mutableSetOf(),
        )

        @EventSourcingHandler
        fun evolve(event: BasketItemSetEvent) {
            itemProductIds.add(event.productId)
        }

        @EventSourcingHandler
        fun evolve(event: BasketItemDeletedEvent) {
            itemProductIds.remove(event.productId)
        }

        @EventSourcingHandler
        fun evolve(event: BasketClearedEvent) {
            itemProductIds.clear()
        }
    }
}
