package jp.inaba.message.user.command

import jp.inaba.core.domain.user.UserId
import jp.inaba.message.UseCaseResult
import jp.inaba.message.Error
import org.axonframework.modelling.annotation.TargetEntityId

data class CreateUserCommand(
    @get:TargetEntityId
    val id: UserId,
    val subject: String,
)

class CreateUserResult private constructor(override val success: Boolean, override val error: Error?) : UseCaseResult() {
    companion object {
        fun success(): CreateUserResult = CreateUserResult(true, null)
        fun userAlreadyExists(): CreateUserResult = CreateUserResult(false, Error("すでに登録されたユーザーです"))
        fun alreadyLinkedSubject(): CreateUserResult = CreateUserResult(false, Error("すでにリンクされたsubjectです"))
    }
}
