package jp.inaba.identity.service.infrastructure.projector.user

import io.github.oshai.kotlinlogging.KotlinLogging
import org.axonframework.config.EventProcessingConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.util.concurrent.TimeUnit

@Configuration
@EnableScheduling
class UserProjectorDeadEventRetryer (
    private val epc: EventProcessingConfiguration
){
    private val logger = KotlinLogging.logger {}

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    fun retry() {
        val maybeProcessor = epc.sequencedDeadLetterProcessor(UserProjectorEventProcessor.PROCESSOR_NAME)
        if(maybeProcessor.isPresent) {
            val processor = maybeProcessor.get()
            while(processor.processAny()) {
                logger.info { "DeadLetterQueueの中身を適応できました。" }
            }
        }
    }
}