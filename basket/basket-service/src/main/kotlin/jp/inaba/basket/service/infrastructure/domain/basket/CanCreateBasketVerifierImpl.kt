package jp.inaba.basket.service.infrastructure.domain.basket

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import jp.inaba.basket.share.domain.basket.CreateBasketError
import jp.inaba.basket.service.domain.basket.CanCreateBasketVerifier
import jp.inaba.basket.service.infrastructure.jpa.lookupbasket.LookupBasketJpaRepository
import jp.inaba.identity.api.domain.user.FindUserByIdError
import jp.inaba.identity.api.domain.user.FindUserByIdQuery
import jp.inaba.identity.api.domain.user.UserId
import jp.inaba.identity.api.domain.user.findUserById
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Service

@Service
class CanCreateBasketVerifierImpl(
    private val queryGateway: QueryGateway,
    private val lookupBasketRepository: LookupBasketJpaRepository,
) : CanCreateBasketVerifier {
    override fun checkUserExits(userId: UserId): Result<Unit, CreateBasketError> {
        val query = FindUserByIdQuery(userId)

        val result = queryGateway.findUserById(query)

        return result.mapBoth(
            success = {
                Ok(Unit)
            },
            failure = {
                when (it) {
                    FindUserByIdError.USER_NOT_FOUND -> Err(CreateBasketError.USER_NOT_FOUND)
                }
            },
        )
    }

    override fun checkUserHasNoBasket(userId: UserId): Result<Unit, CreateBasketError> {
        return if (lookupBasketRepository.existsByUserId(userId.value)) {
            Err(CreateBasketError.BASKET_ALREADY_EXISTS)
        } else {
            Ok(Unit)
        }
    }
}
