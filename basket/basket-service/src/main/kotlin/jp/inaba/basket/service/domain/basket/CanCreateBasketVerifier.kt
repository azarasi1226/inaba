package jp.inaba.basket.service.domain.basket

import com.github.michaelbull.result.Result
import jp.inaba.basket.api.domain.basket.CreateBasketError
import jp.inaba.identity.api.domain.user.UserId

interface CanCreateBasketVerifier {
    fun checkUserExits(userId: UserId): Result<Unit, CreateBasketError>

    fun checkUserHasNoBasket(userId: UserId): Result<Unit, CreateBasketError>
}
