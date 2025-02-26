package jp.inaba.service.application.projector.lookupuser

import jp.inaba.service.application.projector.LookupProjectorEventProcessorBase
import org.springframework.context.annotation.Configuration

@Configuration
class LookupUserProjectorEventProcessor : LookupProjectorEventProcessorBase() {
    companion object {
        const val PROCESSOR_NAME = "lookup-user-projector"
    }

    override val processorName: String
        get() = PROCESSOR_NAME
}
