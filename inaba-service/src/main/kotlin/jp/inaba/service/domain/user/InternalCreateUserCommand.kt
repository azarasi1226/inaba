package jp.inaba.service.domain.user

import jp.inaba.core.domain.user.UserId
import jp.inaba.message.user.command.UserAggregateCommand

data class InternalCreateUserCommand(
    override val id: UserId,
    val subject: String,
) : UserAggregateCommand
