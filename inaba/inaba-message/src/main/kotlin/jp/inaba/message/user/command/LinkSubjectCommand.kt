package jp.inaba.message.user.command

import jp.inaba.core.domain.user.UserId

data class LinkSubjectCommand(
    override val id: UserId,
    val subject: String,
) : UserAggregateCommand
