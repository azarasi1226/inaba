package jp.inaba.basket.service.infrastructure.projector.basketitem

import io.github.oshai.kotlinlogging.KotlinLogging
import org.axonframework.config.EventProcessingConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.util.concurrent.TimeUnit

@Configuration
@EnableScheduling
class BasketItemProjectorDeadEventRetryer (
    private val epc: EventProcessingConfiguration
){
    private val logger = KotlinLogging.logger {}

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    fun retry() {
        val maybeProcessor = epc.sequencedDeadLetterProcessor(BasketItemProjectorEventProcessor.PROCESSOR_NAME)
        if(maybeProcessor.isPresent) {
            logger.info { "BasketItemProjectorDeadEventを再生します。" }

            val processor = maybeProcessor.get()
            while(processor.processAny()) {
                logger.info { "Productを復元しました" }
            }
        }
    }
}