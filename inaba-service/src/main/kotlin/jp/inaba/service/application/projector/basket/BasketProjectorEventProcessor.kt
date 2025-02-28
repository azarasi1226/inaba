package jp.inaba.service.application.projector.basket

import jp.inaba.service.application.projector.ProjectorEventProcessorBase
import org.springframework.context.annotation.Configuration

@Configuration
class BasketProjectorEventProcessor : ProjectorEventProcessorBase() {
    companion object {
        const val PROCESSOR_NAME = "basket-projector"
    }

    override val processorName: String
        get() = PROCESSOR_NAME
}
