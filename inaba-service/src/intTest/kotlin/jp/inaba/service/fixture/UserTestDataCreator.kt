package jp.inaba.service.fixture

import jp.inaba.core.domain.user.UserId
import jp.inaba.service.domain.user.InternalCreateUserCommand
import org.axonframework.commandhandling.gateway.CommandGateway

class UserTestDataCreator(
  private val commandGateway: CommandGateway
) {
  fun handle(
    id: UserId = UserId(),
    subject: String = "sample-subject",
  ) : UserId {
    val command = InternalCreateUserCommand(
      id = id,
      subject = subject,
    )

    commandGateway.sendAndWait<Any>(command)

    return id
  }
}