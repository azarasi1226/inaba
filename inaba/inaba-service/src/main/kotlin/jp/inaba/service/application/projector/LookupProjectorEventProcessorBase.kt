package jp.inaba.service.application.projector

import org.axonframework.config.EventProcessingConfigurer
import org.axonframework.eventhandling.PropagatingErrorHandler
import org.springframework.beans.factory.annotation.Autowired

abstract class LookupProjectorEventProcessorBase {
    abstract val processorName: String

    @Autowired
    fun config(epc: EventProcessingConfigurer) {
        epc.registerSubscribingEventProcessor(processorName)
            // LookupTableの書き込みに失敗した場合、Eventが発行されたこともなかったことにする。
            .registerListenerInvocationErrorHandler(processorName) { PropagatingErrorHandler.INSTANCE }
    }
}