package jp.inaba.message.user.command

import org.axonframework.messaging.commandhandling.gateway.CommandGateway

fun CommandGateway.createUser(command: CreateUserCommand): CreateUserResult {
  return sendAndWait(command, CreateUserResult::class.java);
}

fun CommandGateway.deleteUser(command: DeleteUserCommand): DeleteUserResult {
  return sendAndWait(command, DeleteUserResult::class.java);
}
