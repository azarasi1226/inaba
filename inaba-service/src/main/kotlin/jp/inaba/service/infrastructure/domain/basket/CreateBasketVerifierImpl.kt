package jp.inaba.service.infrastructure.domain.basket

import jp.inaba.core.domain.user.UserId
import jp.inaba.service.domain.basket.CreateBasketVerifier
import jp.inaba.service.domain.user.UserAggregate
import jp.inaba.service.infrastructure.jooq.generated.tables.references.LOOKUP_BASKETS
import org.axonframework.modelling.command.Repository
import org.jooq.DSLContext
import org.springframework.stereotype.Service

@Service
class CreateBasketVerifierImpl(
    private val dsl: DSLContext,
    private val userRepository: Repository<UserAggregate>,
) : CreateBasketVerifier {
    override fun isUserNotFound(userId: UserId): Boolean =
        try {
            userRepository.load(userId.value)
            false
        } catch (_: Exception) {
            true
        }

    override fun isLinkedToUser(userId: UserId): Boolean =
        dsl.fetchExists(
            dsl.selectOne().from(LOOKUP_BASKETS).where(LOOKUP_BASKETS.USER_ID.eq(userId.value)),
        )
}
