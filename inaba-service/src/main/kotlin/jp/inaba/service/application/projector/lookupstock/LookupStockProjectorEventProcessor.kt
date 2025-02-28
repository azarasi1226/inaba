package jp.inaba.service.application.projector.lookupstock

import jp.inaba.service.application.projector.LookupProjectorEventProcessorBase
import org.springframework.context.annotation.Configuration

@Configuration
class LookupStockProjectorEventProcessor : LookupProjectorEventProcessorBase() {
    companion object {
        const val PROCESSOR_NAME = "lookup-stock-projector"
    }

    override val processorName: String
        get() = PROCESSOR_NAME
}
