package jp.inaba.basket.service.presentation.admin

import jp.inaba.basket.service.application.query.projector.basketitem.BasketItemProjectorEventProcessor
import jp.inaba.basket.service.application.query.projector.product.ProductProjectorEventProcessor
import jp.inaba.basket.service.infrastructure.jpa.basketitem.BasketItemJpaRepository
import jp.inaba.basket.service.infrastructure.jpa.product.ProductJpaRepository
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
    private val basketItemJpaRepository: BasketItemJpaRepository,
    private val epc: EventProcessingConfiguration
) {

    @PostMapping("/reconstract")
    fun reset() : ResponseEntity<String> {
        basketItemJpaRepository.deleteAllInBatch()
        productJpaRepository.deleteAllInBatch()

        eventProcessorReset(ProductProjectorEventProcessor.PROCESSOR_NAME)
        eventProcessorReset(BasketItemProjectorEventProcessor.PROCESSOR_NAME)

        return ResponseEntity.ok()
            .body("OK")
    }

    private fun eventProcessorReset(processorName: String) {
        val trackingEventProcessor = epc.eventProcessor(
            processorName,
            TrackingEventProcessor::class.java
        );

        if(trackingEventProcessor.isPresent) {
            val eventProcessor = trackingEventProcessor.get()

            eventProcessor.shutDown()
            eventProcessor.resetTokens()
            eventProcessor.start()
        }
    }
}