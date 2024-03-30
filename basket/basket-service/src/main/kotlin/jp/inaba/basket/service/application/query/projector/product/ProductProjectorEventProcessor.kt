package jp.inaba.basket.service.application.query.projector.product

import org.axonframework.common.jpa.EntityManagerProvider
import org.axonframework.common.transaction.TransactionManager
import org.axonframework.config.EventProcessingConfigurer
import org.axonframework.eventhandling.EventMessage
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration
import org.axonframework.eventhandling.deadletter.jpa.JpaSequencedDeadLetterQueue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
class ProductProjectorEventProcessor
{
    companion object {
        const val PROCESSOR_NAME = "product-projector";
        private const val PROCESSOR_COUNT = 5
        private const val DEAD_LETTER_QUEUE_SEQUENCE = 256
    }

    @Autowired
    fun config(epc: EventProcessingConfigurer) {
        epc.registerTrackingEventProcessorConfiguration(PROCESSOR_NAME) {
            TrackingEventProcessorConfiguration
                .forParallelProcessing(PROCESSOR_COUNT)
                .andInitialSegmentsCount(PROCESSOR_COUNT)
        }
            .registerDeadLetterQueue(PROCESSOR_NAME) {
                JpaSequencedDeadLetterQueue.builder<EventMessage<*>>()
                    .processingGroup(PROCESSOR_NAME)
                    .maxSequences(DEAD_LETTER_QUEUE_SEQUENCE)
                    .maxSequenceSize(DEAD_LETTER_QUEUE_SEQUENCE)
                    .entityManagerProvider(it.getComponent(EntityManagerProvider::class.java))
                    .transactionManager(it.getComponent((TransactionManager::class.java)))
                    .serializer(it.serializer())
                    .build()
            }
    }
}