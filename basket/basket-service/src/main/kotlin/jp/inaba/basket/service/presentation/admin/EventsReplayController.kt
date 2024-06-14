package jp.inaba.basket.service.presentation.admin

import jp.inaba.basket.service.infrastructure.jpa.basket.BasketJpaRepository
import jp.inaba.basket.service.infrastructure.jpa.product.ProductJpaRepository
import jp.inaba.basket.service.infrastructure.projector.basket.BasketProjectorEventProcessor
import jp.inaba.basket.service.infrastructure.projector.product.ProductProjectorEventProcessor
import org.axonframework.config.EventProcessingConfiguration
import org.axonframework.eventhandling.TrackingEventProcessor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class EventsReplayController(
    private val productJpaRepository: ProductJpaRepository,
    private val basketItemJpaRepository: BasketJpaRepository,
    private val epc: EventProcessingConfiguration,
) {
    @PostMapping("/reconstract")
    fun handle(): ResponseEntity<String> {
        basketItemJpaRepository.deleteAllInBatch()
        productJpaRepository.deleteAllInBatch()

        eventProcessorReset(ProductProjectorEventProcessor.PROCESSOR_NAME)
        eventProcessorReset(BasketProjectorEventProcessor.PROCESSOR_NAME)

        return ResponseEntity.ok()
            .body("OK")
    }

    private fun eventProcessorReset(processorName: String) {
        val trackingEventProcessor =
            epc.eventProcessor(
                processorName,
                TrackingEventProcessor::class.java,
            )

        if (trackingEventProcessor.isPresent) {
            val eventProcessor = trackingEventProcessor.get()

            eventProcessor.shutDown()
            eventProcessor.resetTokens()
            eventProcessor.start()
        }
    }
}
