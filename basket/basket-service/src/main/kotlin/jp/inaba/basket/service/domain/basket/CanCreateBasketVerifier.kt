package jp.inaba.basket.service.domain.basket

import com.github.michaelbull.result.Result
import jp.inaba.basket.share.domain.basket.CreateBasketError
import jp.inaba.identity.share.domain.user.UserId

interface CanCreateBasketVerifier {
    fun checkUserExits(userId: UserId): Result<Unit, CreateBasketError>

    fun checkUserHasNoBasket(userId: UserId): Result<Unit, CreateBasketError>
}
