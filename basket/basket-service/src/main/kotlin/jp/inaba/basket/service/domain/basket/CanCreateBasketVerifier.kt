package jp.inaba.basket.service.domain.basket

import com.github.michaelbull.result.Result
import jp.inaba.basket.api.domain.basket.CreateBasketError
import jp.inaba.identity.api.domain.user.UserId

interface CanCreateBasketVerifier {
    fun checkUserExits(userId: UserId): Result<Unit, CreateBasketError>

    // TODO("ネーミング変えたい")
    fun checkBasketExitsForUserid(userId: UserId): Result<Unit, CreateBasketError>
}
