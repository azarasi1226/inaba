package jp.inaba.basket.service.application.command.basket.setbasketitem

import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.service.infrastructure.jpa.basket.BasketJpaRepository
import jp.inaba.basket.service.infrastructure.jpa.product.ProductJpaRepository
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Service

@Service
class SetBasketItemInteractor(
    private val commandGateway: CommandGateway,
    private val basketJpaRepository: BasketJpaRepository,
    private val productJpaRepository: ProductJpaRepository,
) {
    fun handle(inputData: SetBasketItemInputData) {
        // TODO(AggregateNotFoundExceptionで代用できないかな？)
        // 買い物かごの存在チェック
        basketJpaRepository.findById(inputData.basketId.value)
            .orElseThrow { BasketNotFoundException(inputData.basketId) }

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