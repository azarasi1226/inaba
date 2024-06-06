package jp.inaba.basket.api.domain.basket

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import jp.inaba.common.domain.shared.ActionCommandResult
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway

fun CommandGateway.createBasket(command: CreateBasketCommand): Result<Unit, CreateBasketError> {
    val result = this.sendAndWait<ActionCommandResult>(command)

    return if (result.isOk()) {
        Ok(Unit)
    } else {
        val error = CreateBasketError.entries.find { it.errorCode == result.errorCode }

        Err(error!!)
    }
}

fun CommandGateway.setBasketItem(command: SetBasketItemCommand): Result<Unit, SetBasketItemError> {
    val result = this.sendAndWait<ActionCommandResult>(command)

    return if (result.isOk()) {
        Ok(Unit)
    } else {
        val error = SetBasketItemError.entries.find { it.errorCode == result.errorCode }

        Err(error!!)
    }
}

fun CommandGateway.deleteBasketItem(command: DeleteBasketItemCommand) {
    this.sendAndWait<Any>(command)
}

fun CommandGateway.clearBasket(command: ClearBasketCommand) {
    this.sendAndWait<Any>(command)
}

fun QueryGateway.findBasketById(query: FindBasketByIdQuery): FindBasketByIdResult {
    return this.query(query, ResponseTypes.instanceOf(FindBasketByIdResult::class.java))
        .get()
}
