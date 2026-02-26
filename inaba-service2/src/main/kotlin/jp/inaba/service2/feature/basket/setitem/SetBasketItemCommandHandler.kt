package jp.inaba.service2.feature.command.basket.setitem

import jp.inaba.message.InabaEventTag
import jp.inaba.message.basket.command.SetBasketItemCommand
import jp.inaba.message.basket.command.SetBasketItemResult
import jp.inaba.message.basket.event.BasketClearedEvent
import jp.inaba.message.basket.event.BasketItemDeletedEvent
import jp.inaba.message.basket.event.BasketItemSetEvent
import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.message.product.event.ProductDeletedEvent
import jp.inaba.message.user.event.UserCreatedEvent
import jp.inaba.message.user.event.UserDeletedEvent
import org.axonframework.eventsourcing.annotation.EventCriteriaBuilder
import org.axonframework.eventsourcing.annotation.EventSourcingHandler
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator
import org.axonframework.extension.spring.stereotype.EventSourced
import org.axonframework.messaging.commandhandling.annotation.CommandHandler
import org.axonframework.messaging.eventhandling.gateway.EventAppender
import org.axonframework.messaging.eventstreaming.EventCriteria
import org.axonframework.messaging.eventstreaming.Tag
import org.axonframework.modelling.annotation.InjectEntity
import org.springframework.stereotype.Component
import kotlin.reflect.jvm.jvmName

@Component
class SetBasketItemCommandHandler {
    @CommandHandler
    fun handle(
        command: SetBasketItemCommand,
        @InjectEntity state: State,
        eventAppender: EventAppender,
    ): SetBasketItemResult {
        if (!state.userCreated) {
            return SetBasketItemResult.userNotFound()
        }
        if (!state.productCreated) {
            return SetBasketItemResult.productNotFound()
        }

        val isNewProduct = command.productId.value !in state.itemProductIds
        if (isNewProduct && state.itemProductIds.size >= State.MAX_ITEM_KIND_COUNT) {
            return SetBasketItemResult.productMaxKindOver()
        }

        eventAppender.append(
            BasketItemSetEvent(
                userId = command.id.value,
                productId = command.productId.value,
                basketItemQuantity = command.basketItemQuantity.value,
            ),
        )

        return SetBasketItemResult.success()
    }

    @EventSourced(idType = SetBasketItemCommand.TargetId::class)
    class State(
        var userCreated: Boolean,
        var productCreated: Boolean,
        var itemProductIds: MutableSet<String>,
    ) {
        companion object {
            const val MAX_ITEM_KIND_COUNT = 50

            @JvmStatic
            @EventCriteriaBuilder
            private fun resolveCriteria(id: SetBasketItemCommand.TargetId): EventCriteria {
                val userId = id.userId
                val productId = id.productId

                return EventCriteria.either(
                    EventCriteria
                        .havingTags(Tag.of(InabaEventTag.USER_ID, userId.value))
                        .andBeingOneOfTypes(
                            UserCreatedEvent::class.jvmName,
                            UserDeletedEvent::class.jvmName,
                            BasketItemSetEvent::class.jvmName,
                            BasketItemDeletedEvent::class.jvmName,
                            BasketClearedEvent::class.jvmName,
                        ),
                    EventCriteria
                        .havingTags(Tag.of(InabaEventTag.PRODUCT_ID, productId.value))
                        .andBeingOneOfTypes(
                            ProductCreatedEvent::class.jvmName,
                            ProductDeletedEvent::class.jvmName,
                        ),
                )
            }
        }

        @EntityCreator
        constructor() : this(
            userCreated = false,
            productCreated = false,
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
        fun evolve(event: ProductCreatedEvent) {
            productCreated = true
        }

        @EventSourcingHandler
        fun evolve(event: ProductDeletedEvent) {
            productCreated = false
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
