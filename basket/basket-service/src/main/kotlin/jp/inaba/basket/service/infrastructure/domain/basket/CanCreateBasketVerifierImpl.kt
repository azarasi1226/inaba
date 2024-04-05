package jp.inaba.basket.service.infrastructure.domain.basket

import jp.inaba.basket.service.domain.basket.CanCreateBasketVerifier
import jp.inaba.identity.api.domain.user.UserId
import jp.inaba.identity.api.domain.user.UserQueries
import org.axonframework.extensions.kotlin.queryOptional
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Service

@Service
class CanCreateBasketVerifierImpl(
    private val queryGateway: QueryGateway
) : CanCreateBasketVerifier {
    override fun existUser(userId: UserId): Boolean {
        val query = UserQueries.FindByIdQuery(userId)

        return queryGateway.queryOptional<UserQueries.FindByIdResult, UserQueries.FindByIdQuery>(query)
            .get()
            .isPresent;
    }
}