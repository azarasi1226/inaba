package jp.inaba.service.infrastructure.domain.basket

import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.user.UserId
import jp.inaba.service.domain.basket.CreateBasketVerifier
import jp.inaba.service.infrastructure.jpa.lookupbasket.LookupBasketJpaRepository
import jp.inaba.service.infrastructure.jpa.lookupuser.LookupUserJpaRepository
import org.axonframework.eventsourcing.eventstore.EventStore
import org.springframework.stereotype.Service

@Service
class CreateBasketVerifierImpl(
    private val lookupUserJpaRepository: LookupUserJpaRepository,
    private val lookupBasketRepository: LookupBasketJpaRepository,
    private val eventStore: EventStore,
) : CreateBasketVerifier {
    override fun isBasketExits(basketId: BasketId): Boolean {
        return eventStore.lastSequenceNumberFor(basketId.value).isPresent
    }

    override fun isUserNotFound(userId: UserId): Boolean {
        return !lookupUserJpaRepository.existsById(userId.value)
    }

    override fun isLinkedToUser(userId: UserId): Boolean {
        return lookupBasketRepository.existsByUserId(userId.value)
    }
}
