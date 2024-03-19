package jp.inaba.basket.service.application.command.basket.setproduct

import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Service

@Service
class SetBasketItemInteractor(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway
) {
    fun handle(inputData: SetBasketItemInputData) {
        //BasketServiceにデータがあるか確認




    }
}