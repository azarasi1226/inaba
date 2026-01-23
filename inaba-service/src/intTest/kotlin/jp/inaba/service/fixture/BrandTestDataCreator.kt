package jp.inaba.service.fixture

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.brand.BrandName
import jp.inaba.message.brand.command.CreateBrandCommand
import org.axonframework.commandhandling.gateway.CommandGateway

class BrandTestDataCreator(
    private val commandGateway: CommandGateway,
) {
    fun create(
        id: BrandId = BrandId(),
        name: BrandName = BrandName("testBrand1"),
    ): BrandId {
        val command =
            CreateBrandCommand(
                id = id,
                name = name,
            )

        commandGateway.sendAndWait<Any>(command)

        return id
    }
}
