package jp.inaba.service.infrastructure.domain.basket

import jp.inaba.core.domain.user.UserId
import jp.inaba.service.domain.basket.CreateBasketVerifier
import jp.inaba.service.infrastructure.jooq.generated.tables.references.LOOKUP_BASKETS
import jp.inaba.service.infrastructure.jooq.generated.tables.references.LOOKUP_USERS
import org.jooq.DSLContext
import org.springframework.stereotype.Service

@Service
class CreateBasketVerifierImpl(
    private val dsl: DSLContext,
) : CreateBasketVerifier {
    override fun isUserNotFound(userId: UserId): Boolean =
        !dsl.fetchExists(
            dsl.selectOne().from(LOOKUP_USERS).where(LOOKUP_USERS.ID.eq(userId.value)),
        )

    override fun isLinkedToUser(userId: UserId): Boolean =
        dsl.fetchExists(
            dsl.selectOne().from(LOOKUP_BASKETS).where(LOOKUP_BASKETS.USER_ID.eq(userId.value)),
        )
}
