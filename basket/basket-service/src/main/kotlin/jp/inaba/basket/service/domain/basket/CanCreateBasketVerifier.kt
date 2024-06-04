package jp.inaba.basket.service.domain.basket

import com.github.michaelbull.result.Result
import jp.inaba.basket.api.domain.basket.BasketErrors
import jp.inaba.identity.api.domain.user.UserId

interface CanCreateBasketVerifier {
    fun checkUserExits(userId: UserId): Result<Unit, BasketErrors.Create>
}
