package jp.inaba.message.auth.command

import jp.inaba.core.domain.user.UserId

data class UpdateIdTokenAttributeForUserIdCommand(
    override val emailAddress: String,
    val userId: UserId,
) : AuthCommand
