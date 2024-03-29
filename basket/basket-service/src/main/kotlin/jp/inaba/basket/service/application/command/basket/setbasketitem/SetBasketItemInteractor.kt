package jp.inaba.basket.service.application.command.basket.setbasketitem

import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.service.infrastructure.jpa.product.ProductJpaRepository
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Service

@Service
class SetBasketItemInteractor(
    private val commandGateway: CommandGateway,
    private val productJpaRepository: ProductJpaRepository,
) {
    fun handle(inputData: SetBasketItemInputData) {
        // 商品の存在チェック
        productJpaRepository.findById(inputData.productId.value)
            .orElseThrow{ ProductNotFoundException(inputData.productId) }

        val command = BasketCommands.SetBasketItem(
            id = inputData.basketId,
            productId = inputData.productId,
            basketItemQuantity = inputData.basketItemQuantity
        )

        commandGateway.sendAndWait<Any>(command)
    }
}