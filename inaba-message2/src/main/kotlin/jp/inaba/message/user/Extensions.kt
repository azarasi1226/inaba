package jp.inaba.message.user

import jp.inaba.message.user.command.CreateUserCommand
import jp.inaba.message.user.command.CreateUserResult
import jp.inaba.message.user.command.DeleteUserCommand
import jp.inaba.message.user.command.DeleteUserResult
import jp.inaba.message.user.query.FindUserByIdQuery
//import jp.inaba.message.user.query.FindUserByIdResult
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.queryhandling.gateway.QueryGateway

fun CommandGateway.createUser(command: CreateUserCommand): CreateUserResult {
  return this.sendAndWait(command, CreateUserResult::class.java);
}

fun CommandGateway.deleteUser(command: DeleteUserCommand): DeleteUserResult {
  return this.sendAndWait(command, DeleteUserResult::class.java);
}

//fun QueryGateway.findUserById(command: FindUserByIdQuery): FindUserByIdResult {
//  return this.query(command, FindUserByIdResult::class.java).join()
//}