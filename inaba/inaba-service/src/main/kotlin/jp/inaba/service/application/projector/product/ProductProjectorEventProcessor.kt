package jp.inaba.service.application.projector.product

import jp.inaba.service.application.projector.ProjectorEventProcessorBase
import org.springframework.context.annotation.Configuration

@Configuration
class ProductProjectorEventProcessor : ProjectorEventProcessorBase() {
    companion object {
        const val PROCESSOR_NAME = "product-projector"
    }

    override val processorName: String
        get() = PROCESSOR_NAME
}
