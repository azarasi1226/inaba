package jp.inaba.basket.service.application.query.basket

import org.axonframework.config.EventProcessingConfigurer
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
class BasketProjectorEventProcessor
{
    companion object {
        const val PROCESSOR_NAME = "basket_processor";
    }

    @Autowired
    fun eventProcessing(epc: EventProcessingConfigurer) {
        val tepConfig = TrackingEventProcessorConfiguration
            .forParallelProcessing(5)
            .andInitialSegmentsCount(5)

        epc.registerTrackingEventProcessorConfiguration(PROCESSOR_NAME) { tepConfig }
    }
}