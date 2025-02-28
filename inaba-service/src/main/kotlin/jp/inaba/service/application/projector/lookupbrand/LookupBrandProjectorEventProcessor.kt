package jp.inaba.service.application.projector.lookupbrand

import jp.inaba.service.application.projector.LookupProjectorEventProcessorBase
import org.springframework.context.annotation.Configuration

@Configuration
class LookupBrandProjectorEventProcessor : LookupProjectorEventProcessorBase() {
    companion object {
        const val PROCESSOR_NAME = "lookup-brand-projector"
    }

    override val processorName: String
        get() = PROCESSOR_NAME
}
