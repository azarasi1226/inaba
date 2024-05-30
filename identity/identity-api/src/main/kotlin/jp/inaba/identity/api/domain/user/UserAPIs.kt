package jp.inaba.identity.api.domain.user

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway

fun CommandGateway.createUser(command: UserCommands.Create) {
    this.sendAndWait<Any>(command)
}

fun CommandGateway.updateProfileInfo(command: UserCommands.UpdateProfileInfo) {
    this.sendAndWait<Any>(command)
}

fun CommandGateway.updateAddressInfo(command: UserCommands.UpdateAddressInfo) {
    this.sendAndWait<Any>(command)
}

fun CommandGateway.updatePaymentInfo(command: UserCommands.UpdatePaymentInfo) {
    this.sendAndWait<Any>(command)
}

fun CommandGateway.deleteUser(command: UserCommands.Delete) {
    this.sendAndWait<Any>(command)
}

fun QueryGateway.findUserById(query: UserQueries.FindByIdQuery): Result<UserQueries.FindByIdResult, UserErrors.FindById> {
    val result =
        this.query(query, ResponseTypes.optionalInstanceOf(UserQueries.FindByIdResult::class.java))
            .get()

    return if (result.isPresent) {
        Ok(result.get())
    } else {
        Err(UserErrors.FindById.USER_NOT_FOUND)
    }
}
