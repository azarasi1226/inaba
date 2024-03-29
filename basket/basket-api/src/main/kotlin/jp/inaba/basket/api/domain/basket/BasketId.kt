package jp.inaba.basket.api.domain.basket

import jp.inaba.identity.api.domain.user.UserId

// MEMO: UserIdのラッパーIDみたいになっているがこれは、AxonFrameworkで集約が違っても同じIDを使ってはいけない縛りがあるため
// 本当はUserIdそのまま使いたいよおお( ；∀；)
data class BasketId(val value: String) {
    companion object {
        private const val ID_PREFIX = "basket-"
        fun fromUserId(userId: UserId): BasketId {
            return BasketId(ID_PREFIX + userId.value)
        }
    }
    init {
        if(!value.startsWith(ID_PREFIX)) {
            throw Exception("basketIdのprefixには[${ID_PREFIX}]が必要です。現在の値は[${value}]")
        }

        // UserIdがインスタンス化できなければ例外が起きるので、これが検証になる
        val maybeUserId = value.removePrefix(ID_PREFIX)
        UserId(maybeUserId)
    }

    override fun toString(): String {
        return value
    }
}