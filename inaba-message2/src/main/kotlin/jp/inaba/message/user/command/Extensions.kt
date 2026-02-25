package jp.inaba.message.user.command

import jp.inaba.message.user.query.FindUserByIdQuery
import jp.inaba.message.user.query.FindUserByIdResult
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.queryhandling.gateway.QueryGateway

fun CommandGateway.createUser(command: CreateUserCommand): CreateUserResult {
  return sendAndWait(command, CreateUserResult::class.java);
}

fun CommandGateway.deleteUser(command: DeleteUserCommand): DeleteUserResult {
  return sendAndWait(command, DeleteUserResult::class.java);
}

fun QueryGateway.findUserById(command: FindUserByIdQuery): FindUserByIdResult {
  return query(command, FindUserByIdResult::class.java).join()
}