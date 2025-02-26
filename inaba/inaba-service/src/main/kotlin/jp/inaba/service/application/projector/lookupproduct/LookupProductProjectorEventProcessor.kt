package jp.inaba.service.application.projector.lookupproduct

import jp.inaba.service.application.projector.LookupProjectorEventProcessorBase
import org.springframework.context.annotation.Configuration

@Configuration
class LookupProductProjectorEventProcessor : LookupProjectorEventProcessorBase() {
    companion object {
        const val PROCESSOR_NAME = "lookup-product-projector"
    }

    override val processorName: String
        get() = PROCESSOR_NAME
}
