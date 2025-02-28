package jp.inaba.service.application.projector.usermetadata

import jp.inaba.service.application.projector.ProjectorEventProcessorBase
import org.springframework.context.annotation.Configuration

@Configuration
class UserMetadataProjectorEventProcessor : ProjectorEventProcessorBase() {
    companion object {
        const val PROCESSOR_NAME = "user-metadata-projector"
    }

    override val processorName: String
        get() = PROCESSOR_NAME
}
