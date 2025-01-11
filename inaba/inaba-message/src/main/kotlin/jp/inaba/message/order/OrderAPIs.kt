package jp.inaba.message.order

import jp.inaba.message.order.command.IssueOrderCommand
import org.axonframework.commandhandling.gateway.CommandGateway

fun CommandGateway.issueOrder(command: IssueOrderCommand) {
    this.sendAndWait<Any>(command)
}

// fun QueryGateway.findOrderByUserId(query: FindBasketByIdQuery): Result<FindBasketByIdResult, FindBasketByIdError> {
//    val maybeResult =
//        this.query(query, ResponseTypes.optionalInstanceOf(FindBasketByIdResult::class.java))
//            .get()
//
//    return if (maybeResult.isPresent) {
//        Ok(maybeResult.get())
//    } else {
//        Err(FindBasketByIdError.BASKET_NOT_FOUND)
//    }
// }
