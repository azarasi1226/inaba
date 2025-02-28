package jp.inaba.message.user.command

import jp.inaba.core.domain.user.UserId
import org.axonframework.commandhandling.RoutingKey

data class CreateUserCommand(
    @RoutingKey
    val id: UserId,
    val subject: String,
)
