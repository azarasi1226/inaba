package jp.inaba.service.domain.basket

import com.github.michaelbull.result.Result
import jp.inaba.core.domain.basket.CreateBasketError
import jp.inaba.core.domain.user.UserId

interface CanCreateBasketVerifier {
    fun checkUserExits(userId: UserId): Result<Unit, CreateBasketError>

    fun checkUserHasNoBasket(userId: UserId): Result<Unit, CreateBasketError>
}
