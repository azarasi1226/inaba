package jp.inaba.message.basket

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import jp.inaba.core.domain.basket.CreateBasketError
import jp.inaba.core.domain.basket.FindBasketByIdError
import jp.inaba.core.domain.basket.SetBasketItemError
import jp.inaba.core.domain.common.ActionCommandResult
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

fun CommandGateway.deleteBasket(command: DeleteBasketCommand) {
    this.sendAndWait<Any>(command)
}

fun QueryGateway.findBasketById(query: FindBasketByIdQuery): Result<FindBasketByIdResult, FindBasketByIdError> {
    val maybeResult =
        this.query(query, ResponseTypes.optionalInstanceOf(FindBasketByIdResult::class.java))
            .get()

    return if (maybeResult.isPresent) {
        Ok(maybeResult.get())
    } else {
        Err(FindBasketByIdError.BASKET_NOT_FOUND)
    }
}
