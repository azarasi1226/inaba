package jp.inaba.service.infrastructure.projector.lookupuser

import org.axonframework.config.EventProcessingConfigurer
import org.axonframework.eventhandling.PropagatingErrorHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
class LookupUserProjectorEventProcessor {
    companion object {
        const val PROCESSOR_NAME = "lookup-user-projector"
    }

    @Autowired
    fun config(epc: EventProcessingConfigurer) {
        epc.registerSubscribingEventProcessor(PROCESSOR_NAME)
            .registerListenerInvocationErrorHandler(PROCESSOR_NAME) { PropagatingErrorHandler.INSTANCE }
    }
}
