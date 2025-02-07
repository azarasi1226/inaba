package jp.inaba.service.domain.user

import jp.inaba.core.domain.basket.BasketId
import jp.inaba.message.user.command.LinkBasketIdCommand
import jp.inaba.message.user.command.LinkSubjectCommand
import jp.inaba.message.user.event.BasketIdLinkedEvent
import jp.inaba.message.user.event.SubjectLinkedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateLifecycle

class UserMetadata {
    private lateinit var subject: String
    private lateinit var basketId: BasketId

    @CommandHandler
    fun handle(command: LinkSubjectCommand) {
        val event =
            SubjectLinkedEvent(
                id = command.id.value,
                subject = command.subject,
            )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: LinkBasketIdCommand) {
        val event =
            BasketIdLinkedEvent(
                id = command.id.value,
                basketId = command.basketId.value,
            )

        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: SubjectLinkedEvent) {
        subject = event.subject
    }

    @EventSourcingHandler
    fun on(event: BasketIdLinkedEvent) {
        basketId = BasketId(event.basketId)
    }
}
