package jp.inaba.service.application.projector.order

import jp.inaba.service.application.projector.ProjectorEventProcessorBase
import org.springframework.context.annotation.Configuration

@Configuration
class OrderProjectorEventProcessor : ProjectorEventProcessorBase() {
    companion object {
        const val PROCESSOR_NAME = "order-projector"
    }

    override val processorName: String
        get() = PROCESSOR_NAME
}
