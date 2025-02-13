package jp.inaba.service.domain.basket

import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.user.UserId

interface CreateBasketVerifier {
    fun isBasketExits(basketId: BasketId): Boolean

    fun isUserNotFound(userId: UserId): Boolean

    fun isLinkedToUser(userId: UserId): Boolean
}
