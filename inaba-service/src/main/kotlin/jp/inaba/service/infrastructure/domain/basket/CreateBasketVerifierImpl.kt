package jp.inaba.service.infrastructure.domain.basket

import jp.inaba.core.domain.user.UserId
import jp.inaba.service.domain.basket.CreateBasketVerifier
import jp.inaba.service.infrastructure.jooq.generated.tables.references.LOOKUP_BASKET
import jp.inaba.service.infrastructure.jooq.generated.tables.references.LOOKUP_USER
import org.jooq.DSLContext
import org.springframework.stereotype.Service

@Service
class CreateBasketVerifierImpl(
    private val dsl: DSLContext,
) : CreateBasketVerifier {
    override fun isUserNotFound(userId: UserId): Boolean =
        !dsl.fetchExists(
            dsl.selectOne().from(LOOKUP_USER).where(LOOKUP_USER.ID.eq(userId.value)),
        )

    override fun isLinkedToUser(userId: UserId): Boolean =
        dsl.fetchExists(
            dsl.selectOne().from(LOOKUP_BASKET).where(LOOKUP_BASKET.USER_ID.eq(userId.value)),
        )
}
