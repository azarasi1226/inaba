package jp.inaba.basket.api.domain.basket

import jp.inaba.identity.api.domain.user.UserId

// MEMO: UserIdのラッパーIDみたいになっているがこれは、AxonFrameworkで集約が違っても同じIDを使ってはいけない縛りがあるため
// 例えば、BasketIdとUserIdの集約IDを一緒にすることができない....だからUserIdからBasketIdを生成している。
// 本当はUserIdそのまま使いたいよおお( ；∀；)
data class BasketId(val value: String) {
    companion object {
        private const val PREFIX = "basket-"
    }

    constructor(userId: UserId) : this("${PREFIX}${userId.value}")

    init {
        if(!value.startsWith(PREFIX)) {
            throw Exception("basketIdのprefixには[${PREFIX}]が必要です。現在の値は[${value}]")
        }

        // UserIdがインスタンス化できなければ例外が起きるので、これが検証になる
        val maybeUserId = value.removePrefix(PREFIX)
        UserId(maybeUserId)
    }

    override fun toString(): String {
        return value
    }
}