package jp.inaba.basket.service.application.query.basket

import io.github.oshai.kotlinlogging.KotlinLogging
import org.axonframework.config.EventProcessingConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import java.util.concurrent.TimeUnit

@Configuration
class BasketProjectorDeadEventRetryer (
    private val epc: EventProcessingConfiguration
){
    private val logger = KotlinLogging.logger {}

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    fun retry() {
        logger.info { "BasketProjectorDeadLetterを再生します。" }

        val processor = epc.sequencedDeadLetterProcessor(BasketProjectorEventProcessor.PROCESSOR_NAME)
            .orElseThrow()

        processor.processAny()
    }
}