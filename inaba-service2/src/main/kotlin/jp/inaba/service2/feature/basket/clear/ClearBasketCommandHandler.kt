package jp.inaba.service2.feature.command.basket.clear

import jp.inaba.core.domain.user.UserId
import jp.inaba.message.InabaEventTag
import jp.inaba.message.basket.command.ClearBasketCommand
import jp.inaba.message.basket.command.ClearBasketResult
import jp.inaba.message.basket.event.BasketClearedEvent
import jp.inaba.message.basket.event.BasketItemDeletedEvent
import jp.inaba.message.basket.event.BasketItemSetEvent
import jp.inaba.message.user.event.UserCreatedEvent
import jp.inaba.message.user.event.UserDeletedEvent
import org.axonframework.eventsourcing.annotation.EventSourcingHandler
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator
import org.axonframework.extension.spring.stereotype.EventSourced
import org.axonframework.messaging.commandhandling.annotation.CommandHandler
import org.axonframework.messaging.eventhandling.gateway.EventAppender
import org.axonframework.modelling.annotation.InjectEntity
import org.springframework.stereotype.Component

@Component
class ClearBasketCommandHandler {
    @CommandHandler
    fun handle(
        command: ClearBasketCommand,
        @InjectEntity state: State,
        eventAppender: EventAppender,
    ): ClearBasketResult {
        if (!state.userCreated) {
            return ClearBasketResult.userNotFound()
        }
        if (state.itemProductIds.isEmpty()) {
            return ClearBasketResult.success()
        }

        eventAppender.append(
            BasketClearedEvent(
                userId = command.id.value,
            ),
        )

        return ClearBasketResult.success()
    }

    @EventSourced(tagKey = InabaEventTag.USER_ID, idType = UserId::class)
    class State(
        var userCreated: Boolean,
        var itemProductIds: MutableSet<String>,
    ) {
        @EntityCreator
        constructor() : this(
            userCreated = false,
            itemProductIds = mutableSetOf(),
        )

        @EventSourcingHandler
        fun evolve(event: UserCreatedEvent) {
            userCreated = true
        }

        @EventSourcingHandler
        fun evolve(event: UserDeletedEvent) {
            userCreated = false
        }

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
