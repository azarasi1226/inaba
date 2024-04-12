package jp.inaba.identity.service.application.saga.usersetup

import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.api.domain.basket.createBasket
import org.axonframework.commandhandling.gateway.CommandGateway

class CreateBasketStep(
    private val commandGateway: CommandGateway
) {
    private var onSuccess: (() -> Unit)? = null
    private var onFail: ((Exception?) -> Unit)? = null

    fun onSuccess(onSuccess: () -> Unit): CreateBasketStep {
        this.onSuccess = onSuccess
        return this
    }

    fun onFail(onFail: (Exception?) -> Unit): CreateBasketStep {
        this.onFail = onFail
        return this
    }

    fun execute(command: BasketCommands.Create) {
        try {
            val result = commandGateway.createBasket(command)

            if(result.isOk) {
                onSuccess?.invoke()
            } else {
                onFail?.invoke(null)
            }
        }
        catch(e: Exception) {
            onFail?.invoke(e)
        }
    }
}