package jp.inaba.service.infrastructure.domain.basket

import jp.inaba.core.domain.user.UserId
import jp.inaba.service.domain.basket.CanCreateBasketVerifier
import jp.inaba.service.infrastructure.jpa.lookupbasket.LookupBasketJpaRepository
import jp.inaba.service.infrastructure.jpa.user.UserJpaRepository
import org.springframework.stereotype.Service

@Service
class CanCreateBasketVerifierImpl(
    private val userRepository: UserJpaRepository,
    private val lookupBasketRepository: LookupBasketJpaRepository,
) : CanCreateBasketVerifier {
    override fun isUserNotFound(userId: UserId): Boolean {
        return !userRepository.existsById(userId.value)
    }

    override fun isBasketLinkedToUser(userId: UserId): Boolean {
        return !lookupBasketRepository.existsByUserId(userId.value)
    }
}
