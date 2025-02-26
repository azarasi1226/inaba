package jp.inaba.service.application.projector

import org.axonframework.common.jpa.EntityManagerProvider
import org.axonframework.common.transaction.TransactionManager
import org.axonframework.config.EventProcessingConfigurer
import org.axonframework.eventhandling.EventMessage
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration
import org.axonframework.eventhandling.deadletter.jpa.JpaSequencedDeadLetterQueue
import org.springframework.beans.factory.annotation.Autowired

/**
 * 通常の射影用EventProcessor
 *
 * 例外時、DeadLetterQueueに蓄積される。
 */
abstract class ProjectorEventProcessorBase {
    abstract val processorName: String
    open val processorCount: Int
        get() = 2

    @Autowired
    fun config(epc: EventProcessingConfigurer) {
        epc.registerTrackingEventProcessorConfiguration(processorName) {
            TrackingEventProcessorConfiguration
                .forParallelProcessing(processorCount)
        }
            .registerDeadLetterQueue(processorName) {
                JpaSequencedDeadLetterQueue.builder<EventMessage<*>>()
                    .processingGroup(processorName)
                    .entityManagerProvider(it.getComponent(EntityManagerProvider::class.java))
                    .transactionManager(it.getComponent((TransactionManager::class.java)))
                    .serializer(it.serializer())
                    .build()
            }
    }
}