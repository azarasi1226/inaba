package jp.inaba.basket.service.infrastructure.projector.product

import io.github.oshai.kotlinlogging.KotlinLogging
import org.axonframework.config.EventProcessingConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.util.concurrent.TimeUnit

private val logger = KotlinLogging.logger {}

@Configuration
@EnableScheduling
class ProductProjectorDeadEventRetryer(
    private val epc: EventProcessingConfiguration,
) {
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    fun retry() {
        val maybeProcessor = epc.sequencedDeadLetterProcessor(ProductProjectorEventProcessor.PROCESSOR_NAME)
        if (maybeProcessor.isPresent) {
            val processor = maybeProcessor.get()
            while (processor.processAny()) {
                logger.info { "DeadLetterQueueの中身を適応できました。" }
            }
        }
    }
}
