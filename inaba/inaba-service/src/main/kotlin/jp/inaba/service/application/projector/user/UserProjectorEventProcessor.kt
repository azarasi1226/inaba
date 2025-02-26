package jp.inaba.service.application.projector.user

import jp.inaba.service.application.projector.ProjectorEventProcessorBase
import org.springframework.context.annotation.Configuration

@Configuration
class UserProjectorEventProcessor : ProjectorEventProcessorBase() {
    companion object {
        const val PROCESSOR_NAME = "user-projector"
    }

    override val processorName: String
        get() = PROCESSOR_NAME
}
