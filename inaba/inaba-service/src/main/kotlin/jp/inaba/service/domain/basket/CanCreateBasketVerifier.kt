package jp.inaba.service.domain.basket

import jp.inaba.core.domain.user.UserId

interface CanCreateBasketVerifier {
    fun isUserNotFound(userId: UserId): Boolean

    fun isBasketLinkedToUser(userId: UserId): Boolean
}
