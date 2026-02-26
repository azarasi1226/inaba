package jp.inaba.service2.feature.command.brand.create

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.message.CommandResult
import jp.inaba.message.InabaEventTag
import jp.inaba.message.brand.command.CreateBrandCommand
import jp.inaba.message.brand.command.CreateBrandResult
import jp.inaba.message.brand.event.BrandCreatedEvent
import org.axonframework.eventsourcing.annotation.EventSourcingHandler
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator
import org.axonframework.extension.spring.stereotype.EventSourced
import org.axonframework.messaging.commandhandling.annotation.CommandHandler
import org.axonframework.messaging.eventhandling.gateway.EventAppender
import org.axonframework.modelling.annotation.InjectEntity
import org.springframework.stereotype.Component

@Component
class CreateBrandCommandHandler {
    @CommandHandler
    fun handle(
        command: CreateBrandCommand,
        @InjectEntity state: State,
        eventAppender: EventAppender,
    ): CommandResult {
        if (state.created) {
            return CreateBrandResult.duplicated()
        }

        eventAppender.append(
            BrandCreatedEvent(
                id = command.id.value,
                name = command.name.value,
            ),
        )

        return CreateBrandResult.success()
    }

    @EventSourced(tagKey = InabaEventTag.BRAND_ID, idType = BrandId::class)
    class State(
        var created: Boolean,
    ) {
        @EntityCreator
        constructor() : this(
            created = false,
        )

        @EventSourcingHandler
        fun evolve(event: BrandCreatedEvent) {
            created = true
        }
    }
}
