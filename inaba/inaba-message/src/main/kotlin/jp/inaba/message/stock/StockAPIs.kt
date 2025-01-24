package jp.inaba.message.stock

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import jp.inaba.core.domain.common.ActionCommandResult
import jp.inaba.core.domain.stock.CreateStockError
import jp.inaba.core.domain.stock.DecreaseStockError
import jp.inaba.core.domain.stock.IncreaseStockError
import jp.inaba.message.stock.command.CreateStockCommand
import jp.inaba.message.stock.command.DecreaseStockCommand
import jp.inaba.message.stock.command.IncreaseStockCommand
import org.axonframework.commandhandling.gateway.CommandGateway

fun CommandGateway.createStock(command: CreateStockCommand): Result<Unit, CreateStockError> {
    val result = this.sendAndWait<ActionCommandResult>(command)

    return if (result.isOk()) {
        Ok(Unit)
    } else {
        val error = CreateStockError.entries.find { it.errorCode == result.errorCode }

        Err(error!!)
    }
}

fun CommandGateway.increaseStock(command: IncreaseStockCommand): Result<Unit, IncreaseStockError> {
    val result = this.sendAndWait<ActionCommandResult>(command)

    return if (result.isOk()) {
        Ok(Unit)
    } else {
        val error = IncreaseStockError.entries.find { it.errorCode == result.errorCode }

        Err(error!!)
    }
}

fun CommandGateway.decreaseStock(command: DecreaseStockCommand): Result<Unit, DecreaseStockError> {
    val result = this.sendAndWait<ActionCommandResult>(command)

    return if (result.isOk()) {
        Ok(Unit)
    } else {
        val error = DecreaseStockError.entries.find { it.errorCode == result.errorCode }

        Err(error!!)
    }
}
