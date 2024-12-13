package jp.inaba.identity.service.application.saga.usersetup

import jp.inaba.basket.api.domain.basket.event.BasketCreatedEvent
import jp.inaba.basket.share.domain.basket.BasketId
import jp.inaba.identity.api.domain.external.auth.event.SignupConfirmedEvent
import jp.inaba.identity.api.domain.user.event.UserCreatedEvent
import jp.inaba.identity.share.domain.user.UserId

// MEMO: これもしかしたらJsonのやつでprivate onにしないといけないかも
class UserSetupSagaState private constructor(
    val emailAddress: String,
) {
    private var _userId: UserId? = null
    val userId: UserId
        get() = _userId ?: throw IllegalStateException("userId が初期化されていません。associateUserCreatedEventが呼ばれていません。")

    private var _basketId: BasketId? = null
    val basketId: BasketId
        get() = _basketId ?: throw IllegalStateException("basketId が初期化されていません。associateBasketCreatedEventが呼ばれていません。")

    constructor(event: SignupConfirmedEvent) : this(emailAddress = event.emailAddress)

    fun associateUserCreatedEvent(event: UserCreatedEvent) {
        _userId = UserId(event.id)
    }

    fun associateBasketCreatedEvent(event: BasketCreatedEvent) {
        _basketId = BasketId(event.id)
    }
}
