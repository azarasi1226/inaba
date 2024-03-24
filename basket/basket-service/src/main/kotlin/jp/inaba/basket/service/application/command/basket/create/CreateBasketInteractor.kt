package jp.inaba.basket.service.application.command.basket.create

import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.service.infrastructure.jpa.basket.BasketJpaRepository
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Service

@Service
class CreateBasketInteractor(
    private val basketJpaRepository: BasketJpaRepository,
    private val commandGateway: CommandGateway,
) {
    fun handle(inputData: CreateBasketInputData): CreateBasketOutputData {
        // ユーザーIDの重複チェック
        if(basketJpaRepository.existsByUserId(inputData.userId)) {
          throw UserDuplicatedException(inputData.userId)
        }

        val basketId = BasketId()
        val command = BasketCommands.Create(basketId, inputData.userId)

        commandGateway.sendAndWait<Any>(command)

        return CreateBasketOutputData(basketId.value)
    }
}