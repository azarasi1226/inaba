package jp.inaba.service2.features.command.product.create

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.product.ProductId
import jp.inaba.message.InabaEventTag
import jp.inaba.message.brand.event.BrandCreatedEvent
import jp.inaba.message.brand.event.BrandDeletedEvent
import jp.inaba.message.product.command.CreateProductCommand
import jp.inaba.message.product.command.CreateProductResult
import jp.inaba.message.product.event.ProductCreatedEvent
import org.axonframework.eventsourcing.annotation.EventCriteriaBuilder
import org.axonframework.eventsourcing.annotation.EventSourcingHandler
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator
import org.axonframework.extension.spring.stereotype.EventSourced
import org.axonframework.messaging.commandhandling.annotation.CommandHandler
import org.axonframework.messaging.core.Message
import org.axonframework.messaging.core.conversion.MessageConverter
import org.axonframework.messaging.core.unitofwork.ProcessingContext
import org.axonframework.messaging.eventhandling.gateway.EventAppender
import org.axonframework.messaging.eventstreaming.EventCriteria
import org.axonframework.messaging.eventstreaming.Tag
import org.axonframework.modelling.EntityIdResolver
import org.axonframework.modelling.annotation.InjectEntity
import org.springframework.stereotype.Component
import kotlin.reflect.jvm.jvmName


@Component
class CreateProductCommandHandler {
    @CommandHandler
    fun handle(
        command: CreateProductCommand,
        @InjectEntity(idResolver = SubscriptionIdResolver::class) state: State,
        eventAppender: EventAppender,
    ): CreateProductResult {
        if (state.created) {
            return CreateProductResult.alreadyExists()
        }
        if (!state.brandExists) {
            return CreateProductResult.brandNotFound()
        }

        eventAppender.append(
            ProductCreatedEvent(
                id = command.id.value,
                brandId = command.brandId.value,
                name = command.name.value,
                description = command.description.value,
                imageUrl = command.imageUrl?.value,
                price = command.price.value,
                quantity = command.quantity.value,
            ),
        )

        return CreateProductResult.success()
    }
}

@EventSourced
class State(
    var created: Boolean,
    var brandExists: Boolean,
) {
    companion object {
        @EventCriteriaBuilder
        fun buildCriteria(id: SubscriptionId): EventCriteria {
            val productId = id.productId
            val brandId = id.brandId

            return EventCriteria
                .havingTags(Tag.of(InabaEventTag.BRAND_ID, brandId.value), Tag.of(InabaEventTag.PRODUCT_ID, productId.value))
                .andBeingOneOfTypes(
                    ProductCreatedEvent::class.jvmName,
                    BrandCreatedEvent::class.jvmName,
                    BrandDeletedEvent::class.jvmName,
                )
        }
    }

    @EntityCreator
    constructor() : this(
        created = false,
        brandExists = false,
    )

    @EventSourcingHandler
    fun evolve(event: ProductCreatedEvent) {
        created = true
    }

    @EventSourcingHandler
    fun evolve(event: BrandCreatedEvent) {
        brandExists = true
    }

    @EventSourcingHandler
    fun evolve(event: BrandDeletedEvent) {
        brandExists = false
    }
}

data class SubscriptionId(val productId: ProductId, val brandId: BrandId)


class SubscriptionIdResolver : EntityIdResolver<SubscriptionId> {
    override fun resolve(command: Message, context: ProcessingContext): SubscriptionId {
        val converter = context.component(MessageConverter::class.java)
        val payload: CreateProductCommand = command.payloadAs(CreateProductCommand::class.java, converter)
        return SubscriptionId(payload.id, payload.brandId)
    }
}