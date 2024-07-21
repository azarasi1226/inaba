package jp.inaba.service.infrastructure.projector.order

import org.axonframework.config.EventProcessingConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.util.concurrent.TimeUnit

@Configuration
@EnableScheduling
class OrderProjectorDeadLetterQueueRetryer(
    private val configuration: EventProcessingConfiguration,
) {
    // TODO(↓の数字DIしたい)
    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    fun retry() {
        val processor = configuration.sequencedDeadLetterProcessor(OrderProjector.PROCESSOR_NAME).get()
        processor.processAny()
    }
}
