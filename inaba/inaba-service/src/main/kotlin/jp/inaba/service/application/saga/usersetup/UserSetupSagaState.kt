package jp.inaba.service.application.saga.usersetup

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.user.UserId
import jp.inaba.message.auth.event.SignupConfirmedEvent
import jp.inaba.message.user.event.UserCreatedEvent
import jp.inaba.message.basket.event.BasketCreatedEvent

// MEMO: これもしかしたらJsonのやつでprivate onにしないといけないかも
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class UserSetupSagaState private constructor(
    val emailAddress: String,
) {

    private var _userId: UserId? = null
    @get:JsonIgnore
    val userId: UserId
        get() = _userId ?: throw IllegalStateException("userId が初期化されていません。associateUserCreatedEventが呼ばれていません。")

    private var _basketId: BasketId? = null
    @get:JsonIgnore
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
