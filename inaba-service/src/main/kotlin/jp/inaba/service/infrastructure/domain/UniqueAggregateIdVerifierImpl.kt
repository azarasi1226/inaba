package jp.inaba.service.infrastructure.domain

import jp.inaba.service.domain.UniqueAggregateIdVerifier
import org.axonframework.eventsourcing.eventstore.EventStore
import org.springframework.stereotype.Service

@Service
class UniqueAggregateIdVerifierImpl(
    private val eventStore: EventStore,
) : UniqueAggregateIdVerifier {
    override fun hasDuplicateAggregateId(id: String): Boolean = eventStore.lastSequenceNumberFor(id).isPresent
}
