package jp.inaba.basket.service.infrastructure.projector.lookupbasket

import jp.inaba.basket.service.infrastructure.projector.basket.BasketProjectorEventProcessor
import org.axonframework.config.EventProcessingConfigurer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
class LookupBasketProjectorEventProcessor {
    companion object {
        const val PROCESSOR_NAME = "lookup-basket-projector"
    }

    @Autowired
    fun config(epc: EventProcessingConfigurer) {
        epc.registerSubscribingEventProcessor(PROCESSOR_NAME)
    }
}