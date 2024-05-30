package jp.inaba.basket.service.infrastructure.domain.basket

import jp.inaba.basket.service.domain.basket.CanCreateBasketVerifier
import jp.inaba.identity.api.domain.user.UserId
import jp.inaba.identity.api.domain.user.UserQueries
import jp.inaba.identity.api.domain.user.findUserById
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Service

@Service
class CanCreateBasketVerifierImpl(
    private val queryGateway: QueryGateway,
) : CanCreateBasketVerifier {
    override fun existUser(userId: UserId): Boolean {
        val query = UserQueries.FindByIdQuery(userId)

        val result = queryGateway.findUserById(query)

        return result.isOk
    }
}
