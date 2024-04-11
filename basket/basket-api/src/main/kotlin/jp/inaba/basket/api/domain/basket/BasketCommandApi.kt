package jp.inaba.basket.api.domain.basket

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import jp.inaba.common.domain.shared.ActionCommandResult
import org.axonframework.commandhandling.gateway.CommandGateway

fun CommandGateway.createBasket(command: BasketCommands.Create): Result<Unit, BasketCommandErrors.Create> {
    val result = this.sendAndWait<ActionCommandResult>(command)

    //TODO(ここら辺、ジェネリクスメソッド使ってシンプルにできそう)
    return if(result.isOk()) {
        Ok(Unit)
    }
    else {
        val error = BasketCommandErrors.Create.entries.find { it.errorCode == result.errorCode }

        Err(error!!)
    }
}

fun CommandGateway.setBasketItem(command: BasketCommands.SetBasketItem): Result<Unit, BasketCommandErrors.SetBasketItem> {
    val result = this.sendAndWait<ActionCommandResult>(command)

    return if(result.isOk()) {
        Ok(Unit)
    }
    else {
        val error = BasketCommandErrors.SetBasketItem.entries.find { it.errorCode == result.errorCode }

        Err(error!!)
    }
}

fun CommandGateway.deleteBasketItem(command: BasketCommands.DeleteBasketItem) {
    this.sendAndWait<Any>(command)
}

fun CommandGateway.clearBasket(command: BasketCommands.Clear) {
    this.sendAndWait<Any>(command)
}