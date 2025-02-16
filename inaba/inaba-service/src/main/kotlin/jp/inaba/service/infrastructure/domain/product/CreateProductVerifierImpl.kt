package jp.inaba.service.infrastructure.domain.product

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.product.ProductId
import jp.inaba.message.brand.command.VerifyBrandExistenceCommand
import jp.inaba.service.domain.product.CreateProductVerifier
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventsourcing.eventstore.EventStore
import org.springframework.stereotype.Service

@Service
class CreateProductVerifierImpl(
    private val commandGateway: CommandGateway,
    private val eventStore: EventStore
) : CreateProductVerifier {
    override fun isProductExists(productId: ProductId): Boolean {
        return eventStore.lastSequenceNumberFor(productId.value).isPresent
    }

    override fun isBrandNotFound(brandId: BrandId): Boolean {
        val command =
            VerifyBrandExistenceCommand(
                id = brandId,
            )

        return try {
            commandGateway.sendAndWait<Any>(command)
            false
        } catch (e: Exception) {
            true
        }
    }
}
