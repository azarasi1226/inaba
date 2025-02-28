package jp.inaba.service.application.projector.stock

import jp.inaba.service.application.projector.ProjectorEventProcessorBase
import org.springframework.context.annotation.Configuration

@Configuration
class StockProjectorEventProcessor : ProjectorEventProcessorBase() {
    companion object {
        const val PROCESSOR_NAME = "stock-projector"
    }

    override val processorName: String
        get() = PROCESSOR_NAME
}
