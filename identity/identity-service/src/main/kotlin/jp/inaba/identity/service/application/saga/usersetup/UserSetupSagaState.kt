package jp.inaba.identity.service.application.saga.usersetup

import jp.inaba.identity.api.domain.external.auth.AuthEvents
import jp.inaba.identity.api.domain.user.UserCreatedEvent
import jp.inaba.identity.api.domain.user.UserId

class UserSetupSagaState private constructor(
    val emailAddress: String,
) {
    private var _userId: UserId? = null
    val userId: UserId
        get() = _userId ?: throw IllegalStateException("userId が初期化されていません。")

    constructor(event: AuthEvents.SignupConfirmed) : this(emailAddress = event.emailAddress)

    fun associateUserCreatedEvent(event: UserCreatedEvent) {
        _userId = UserId(event.id)
    }
}
