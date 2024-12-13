package jp.inaba.identity.api.domain.user.command

import jp.inaba.identity.share.domain.user.UserId

data class DeleteUserCommand(
    override val id: UserId,
) : UserAggregateCommand