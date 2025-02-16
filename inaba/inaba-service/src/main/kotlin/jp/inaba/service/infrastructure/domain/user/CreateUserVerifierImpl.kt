package jp.inaba.service.infrastructure.domain.user

import jp.inaba.core.domain.user.UserId
import jp.inaba.service.domain.user.CreateUserVerifier
import jp.inaba.service.infrastructure.jpa.lookupuser.LookupUserJpaRepository
import org.axonframework.eventsourcing.eventstore.EventStore
import org.springframework.stereotype.Service

@Service
class CreateUserVerifierImpl(
    private val repository: LookupUserJpaRepository,
    private val eventStore: EventStore
) : CreateUserVerifier {
    override fun isUserExists(userId: UserId): Boolean {
        return eventStore.lastSequenceNumberFor(userId.value).isPresent
    }

    override fun isLinkedSubject(subject: String): Boolean {
        return repository.existsBySubject(subject)
    }
}
