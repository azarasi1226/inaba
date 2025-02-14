package jp.inaba.service.infrastructure.domain.brand

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.message.brand.command.VerifyBrandExistenceCommand
import jp.inaba.service.domain.brand.CreateBrandVerifier
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Service

@Service
class CreateBrandVerifierImpl(
    private val commandGateway: CommandGateway
) : CreateBrandVerifier {
    override fun isBrandExits(brandId: BrandId): Boolean {
        val command = VerifyBrandExistenceCommand(
            id = brandId
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            true
        } catch(e: Exception) {
            false
        }
    }
}