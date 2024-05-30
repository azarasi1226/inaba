package jp.inaba.identity.service.application.saga.usersetup

import jp.inaba.identity.api.domain.external.auth.AuthEvents
import jp.inaba.identity.api.domain.user.UserEvents
import jp.inaba.identity.api.domain.user.UserId

class UserSetupSagaState private constructor(
    val emailAddress: String,
    var userId: UserId? = null,
) {
    companion object {
        fun create(event: AuthEvents.SignupConfirmed): UserSetupSagaState {
            return UserSetupSagaState(emailAddress = event.emailAddress)
        }
    }

    fun associateUserCreatedEvent(event: UserEvents.Created) {
        userId = UserId(event.id)
    }
}
