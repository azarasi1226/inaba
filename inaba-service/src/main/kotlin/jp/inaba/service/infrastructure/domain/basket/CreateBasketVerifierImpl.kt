package jp.inaba.service.infrastructure.domain.basket

import jp.inaba.core.domain.user.UserId
import jp.inaba.service.domain.basket.CreateBasketVerifier
import jp.inaba.service.infrastructure.jpa.lookupbasket.LookupBasketJpaRepository
import jp.inaba.service.infrastructure.jpa.lookupuser.LookupUserJpaRepository
import org.springframework.stereotype.Service

@Service
class CreateBasketVerifierImpl(
    private val lookupUserJpaRepository: LookupUserJpaRepository,
    private val lookupBasketRepository: LookupBasketJpaRepository,
) : CreateBasketVerifier {
    override fun isUserNotFound(userId: UserId): Boolean = !lookupUserJpaRepository.existsById(userId.value)

    override fun isLinkedToUser(userId: UserId): Boolean = lookupBasketRepository.existsByUserId(userId.value)
}
