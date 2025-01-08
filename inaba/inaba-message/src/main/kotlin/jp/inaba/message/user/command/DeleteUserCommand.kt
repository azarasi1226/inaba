package jp.inaba.message.user.command

import jp.inaba.core.domain.user.UserId

data class DeleteUserCommand(
    override val id: UserId,
) : UserAggregateCommand
