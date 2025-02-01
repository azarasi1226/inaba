package jp.inaba.service.infrastructure.projector.lookupproduct

import org.axonframework.config.EventProcessingConfigurer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
class LookupProductProjectorEventProcessor {
    companion object {
        const val PROCESSOR_NAME = "lookup-product-projector"
    }

    @Autowired
    fun config(epc: EventProcessingConfigurer) {
        epc.registerSubscribingEventProcessor(PROCESSOR_NAME)
    }
}