package jp.inaba.message.user.command

import jp.inaba.core.domain.user.UserId
import jp.inaba.message.UseCaseResult
import jp.inaba.message.Error
import org.axonframework.modelling.annotation.TargetEntityId

data class DeleteUserCommand(
    @get:TargetEntityId
    val id: UserId,
)

class DeleteUserResult private constructor(override val success: Boolean, override val error: Error?) : UseCaseResult() {
    companion object {
        fun success(): DeleteUserResult = DeleteUserResult(true, null)
        fun userNotFound(): DeleteUserResult = DeleteUserResult(false, Error("存在しないユーザーです"))
    }
}