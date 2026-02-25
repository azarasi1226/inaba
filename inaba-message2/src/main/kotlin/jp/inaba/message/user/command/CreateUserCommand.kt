package jp.inaba.message.user.command

import jp.inaba.core.domain.user.UserId
import jp.inaba.message.UseCaseResult
import jp.inaba.message.Error

data class CreateUserCommand(
    override val id: UserId,
    val subject: String,
): UserCommand

class CreateUserResult private constructor(override val success: Boolean, override val error: Error?) : UseCaseResult {
    companion object {
        fun success(): CreateUserResult = CreateUserResult(true, null)
        fun userAlreadyExists(): CreateUserResult = CreateUserResult(false, Error("すでに登録されたユーザーです"))
        fun alreadyLinkedSubject(): CreateUserResult = CreateUserResult(false, Error("すでにリンクされたsubjectです"))
    }
}
