package jp.inaba.message.user.command

import jp.inaba.core.domain.user.UserId
import jp.inaba.message.UseCaseResult
import jp.inaba.message.Error

data class DeleteUserCommand(
    override val id: UserId,
) : UserCommand

class DeleteUserResult private constructor(override val success: Boolean, override val error: Error?) : UseCaseResult() {
    companion object {
        fun success(): DeleteUserResult = DeleteUserResult(true, null)
        fun userNotFound(): DeleteUserResult = DeleteUserResult(false, Error("存在しないユーザーです"))
    }
}