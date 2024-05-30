package jp.inaba.service.application.query.order

import org.axonframework.common.jpa.EntityManagerProvider
import org.axonframework.common.transaction.TransactionManager
import org.axonframework.config.EventProcessingConfigurer
import org.axonframework.eventhandling.EventMessage
import org.axonframework.eventhandling.deadletter.jpa.JpaSequencedDeadLetterQueue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
class OrderProjectorEventProcessorConfiguration {
    @Autowired
    fun configureEventProcessing(configurer: EventProcessingConfigurer) {
        configurer.registerDeadLetterQueue(OrderProjector.PROCESSOR_NAME) {
            JpaSequencedDeadLetterQueue.builder<EventMessage<*>>()
                .processingGroup(OrderProjector.PROCESSOR_NAME)
                // TODO(↓の数字DIしたい)
                .maxSequences(256)
                .maxSequenceSize(256)
                .entityManagerProvider(it.getComponent(EntityManagerProvider::class.java))
                .transactionManager(it.getComponent(TransactionManager::class.java))
                .serializer(it.serializer())
                .build()
        }
    }
}
