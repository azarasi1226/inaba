package jp.inaba.service.application.projector.lookupbasket

import jp.inaba.service.application.projector.LookupProjectorEventProcessorBase
import org.springframework.context.annotation.Configuration

@Configuration
class LookupBasketProjectorEventProcessor : LookupProjectorEventProcessorBase() {
    companion object {
        const val PROCESSOR_NAME = "lookup-basket-projector"
    }

    override val processorName: String
        get() = PROCESSOR_NAME
}
