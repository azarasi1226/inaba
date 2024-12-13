package jp.inaba.identity.api.domain.external.auth.command

import jp.inaba.identity.share.domain.user.UserId

data class UpdateIdTokenAttributeForUserIdCommand(
    override val emailAddress: String,
    val userId: UserId,
) : AuthCommand
