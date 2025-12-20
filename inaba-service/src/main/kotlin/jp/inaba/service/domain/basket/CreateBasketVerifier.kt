package jp.inaba.service.domain.basket

import jp.inaba.core.domain.user.UserId

interface CreateBasketVerifier {
    fun isUserNotFound(userId: UserId): Boolean

    fun isLinkedToUser(userId: UserId): Boolean
}
