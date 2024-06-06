package jp.inaba.basket.service.infrastructure.domain.basket

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import jp.inaba.basket.api.domain.basket.CreateBasketError
import jp.inaba.basket.service.domain.basket.CanCreateBasketVerifier
import jp.inaba.identity.api.domain.user.UserErrors
import jp.inaba.identity.api.domain.user.UserId
import jp.inaba.identity.api.domain.user.UserQueries
import jp.inaba.identity.api.domain.user.findUserById
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Service

@Service
class CanCreateBasketVerifierImpl(
    private val queryGateway: QueryGateway,
) : CanCreateBasketVerifier {
    override fun checkUserExits(userId: UserId): Result<Unit, CreateBasketError> {
        val query = UserQueries.FindByIdQuery(userId)

        val result = queryGateway.findUserById(query)

        return result.mapBoth(
            success = {
                Ok(Unit)
            },
            failure = {
                when (it) {
                    UserErrors.FindById.USER_NOT_FOUND -> Err(CreateBasketError.USER_NOT_FOUND)
                }
            },
        )
    }
}
