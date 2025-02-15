package jp.inaba.service.infrastructure.domain.product

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.product.ProductId
import jp.inaba.message.brand.command.VerifyBrandExistenceCommand
import jp.inaba.service.domain.product.CreateProductVerifier
import jp.inaba.service.infrastructure.jpa.lookupproduct.LookupProductJpaRepository
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Service

@Service
class CreateProductVerifierImpl(
    private val repository: LookupProductJpaRepository,
    private val commandGateway: CommandGateway,
) : CreateProductVerifier {
    override fun isProductExists(productId: ProductId): Boolean {
        return repository.existsById(productId.value)
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
