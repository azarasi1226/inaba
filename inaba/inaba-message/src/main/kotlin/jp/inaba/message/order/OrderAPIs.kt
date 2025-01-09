package jp.inaba.message.order

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import jp.inaba.core.domain.basket.FindBasketByIdError
import jp.inaba.message.basket.command.ClearBasketCommand
import jp.inaba.message.basket.command.DeleteBasketCommand
import jp.inaba.message.basket.command.DeleteBasketItemCommand
import jp.inaba.message.basket.query.FindBasketByIdQuery
import jp.inaba.message.basket.query.FindBasketByIdResult
import jp.inaba.message.order.command.IssueOrderCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway

fun CommandGateway.issueOrder(command: IssueOrderCommand) {
    this.sendAndWait<Any>(command)
}

fun QueryGateway.findOrderByUserId(query: FindBasketByIdQuery): Result<FindBasketByIdResult, FindBasketByIdError> {
    val maybeResult =
        this.query(query, ResponseTypes.optionalInstanceOf(FindBasketByIdResult::class.java))
            .get()

    return if (maybeResult.isPresent) {
        Ok(maybeResult.get())
    } else {
        Err(FindBasketByIdError.BASKET_NOT_FOUND)
    }
}
