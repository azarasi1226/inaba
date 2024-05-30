package jp.inaba.basket.service.domain.basket

import jp.inaba.identity.api.domain.user.UserId

interface CanCreateBasketVerifier {
    fun existUser(userId: UserId): Boolean
}
