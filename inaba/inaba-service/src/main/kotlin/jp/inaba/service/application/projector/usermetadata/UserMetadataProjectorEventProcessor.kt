package jp.inaba.service.application.projector.usermetadata

import org.axonframework.common.jpa.EntityManagerProvider
import org.axonframework.common.transaction.TransactionManager
import org.axonframework.config.EventProcessingConfigurer
import org.axonframework.eventhandling.EventMessage
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration
import org.axonframework.eventhandling.deadletter.jpa.JpaSequencedDeadLetterQueue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
class UserMetadataProjectorEventProcessor {
    companion object {
        const val PROCESSOR_NAME = "user-metadata-projector"
        private const val PROCESSOR_COUNT = 2
    }

    @Autowired
    fun config(epc: EventProcessingConfigurer) {
        epc.registerTrackingEventProcessorConfiguration(
            jp.inaba.service.application.projector.usermetadata.UserMetadataProjectorEventProcessor.Companion.PROCESSOR_NAME,
        ) {
            TrackingEventProcessorConfiguration
                .forParallelProcessing(
                    jp.inaba.service.application.projector.usermetadata.UserMetadataProjectorEventProcessor.Companion.PROCESSOR_COUNT,
                )
        }
            .registerDeadLetterQueue(
                jp.inaba.service.application.projector.usermetadata.UserMetadataProjectorEventProcessor.Companion.PROCESSOR_NAME,
            ) {
                JpaSequencedDeadLetterQueue.builder<EventMessage<*>>()
                    .processingGroup(
                        jp.inaba.service.application.projector.usermetadata.UserMetadataProjectorEventProcessor.Companion.PROCESSOR_NAME,
                    )
                    .entityManagerProvider(it.getComponent(EntityManagerProvider::class.java))
                    .transactionManager(it.getComponent((TransactionManager::class.java)))
                    .serializer(it.serializer())
                    .build()
            }
    }
}
