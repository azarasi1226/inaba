package jp.inaba.identity.service.application.saga.usersetup

import jp.inaba.identity.api.domain.external.auth.AuthEvents
import jp.inaba.identity.api.domain.user.UserCreatedEvent
import jp.inaba.identity.api.domain.user.UserId

class UserSetupSagaState private constructor(
    val emailAddress: String,
    var userId: UserId? = null,
) {
    constructor(event: AuthEvents.SignupConfirmed) : this(emailAddress = event.emailAddress)

    fun associateUserCreatedEvent(event: UserCreatedEvent) {
        userId = UserId(event.id)
    }
}
