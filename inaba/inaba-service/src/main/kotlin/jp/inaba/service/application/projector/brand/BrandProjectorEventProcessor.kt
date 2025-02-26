package jp.inaba.service.application.projector.brand

import jp.inaba.service.application.projector.ProjectorEventProcessorBase
import org.springframework.context.annotation.Configuration

@Configuration
class BrandProjectorEventProcessor : ProjectorEventProcessorBase() {
    companion object {
        const val PROCESSOR_NAME = "brand-projector"
    }

    override val processorName: String
        get() = PROCESSOR_NAME
}
