package jp.inaba.service2.features.command.product.create

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
import org.axonframework.messaging.eventhandling.gateway.EventAppender
import org.axonframework.messaging.eventstreaming.EventCriteria
import org.axonframework.messaging.eventstreaming.Tag
import org.axonframework.modelling.annotation.InjectEntity
import org.springframework.stereotype.Component
import kotlin.reflect.jvm.jvmName

@Component
class CreateProductCommandHandler {
    @CommandHandler
    fun handle(
        command: CreateProductCommand,
        @InjectEntity state: CreateProductCommandState,
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

@EventSourced(idType = CreateProductCommand.TargetId::class)
class CreateProductCommandState(
    var created: Boolean,
    var brandExists: Boolean,
) {
    companion object {
        @JvmStatic
        @EventCriteriaBuilder
        private fun resolveCriteria(id: CreateProductCommand.TargetId): EventCriteria {
            val productId = id.productId
            val brandId = id.brandId

            return EventCriteria.either(
                EventCriteria
                    .havingTags(Tag.of(InabaEventTag.PRODUCT_ID, productId.value))
                    .andBeingOneOfTypes(
                        ProductCreatedEvent::class.jvmName,
                    ),
                EventCriteria
                    .havingTags(Tag.of(InabaEventTag.BRAND_ID, brandId.value))
                    .andBeingOneOfTypes(
                        BrandCreatedEvent::class.jvmName,
                        BrandDeletedEvent::class.jvmName,
                    ),
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
