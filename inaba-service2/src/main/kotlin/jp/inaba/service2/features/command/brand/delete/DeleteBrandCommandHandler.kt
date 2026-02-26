package jp.inaba.service2.features.command.brand.delete

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.message.CommandResult
import jp.inaba.message.InabaEventTag
import jp.inaba.message.brand.command.DeleteBrandCommand
import jp.inaba.message.brand.command.DeleteBrandResult
import jp.inaba.message.brand.event.BrandCreatedEvent
import jp.inaba.message.brand.event.BrandDeletedEvent
import org.axonframework.eventsourcing.annotation.EventSourcingHandler
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator
import org.axonframework.extension.spring.stereotype.EventSourced
import org.axonframework.messaging.commandhandling.annotation.CommandHandler
import org.axonframework.messaging.eventhandling.gateway.EventAppender
import org.axonframework.modelling.annotation.InjectEntity
import org.springframework.stereotype.Component

@Component
class DeleteBrandCommandHandler {
    @CommandHandler
    fun handle(
        command: DeleteBrandCommand,
        @InjectEntity state: State,
        eventAppender: EventAppender,
    ): CommandResult {
        if (!state.created) {
            return DeleteBrandResult.notFound()
        }
        // 冪等性を考慮し、すでに削除されている場合は成功を返す
        if (state.deleted) {
            return DeleteBrandResult.success()
        }

        eventAppender.append(
            BrandDeletedEvent(
                id = command.id.value,
            ),
        )

        return DeleteBrandResult.success()
    }

    @EventSourced(tagKey = InabaEventTag.BRAND_ID, idType = BrandId::class)
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
        fun evolve(event: BrandCreatedEvent) {
            created = true
        }

        @EventSourcingHandler
        fun evolve(event: BrandDeletedEvent) {
            deleted = true
        }
    }
}