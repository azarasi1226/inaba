package jp.inaba.basket.service.presentation.admin

import jp.inaba.basket.service.application.query.basket.BasketProjectorEventProcessor
import org.apache.coyote.Response
import org.axonframework.config.EventProcessingConfiguration
import org.axonframework.eventhandling.TrackingEventProcessor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class EventsReplayController(
    private val epc: EventProcessingConfiguration
) {
    @PostMapping("/eventProcessor/{processorName}/reset")
    fun reset(
        @PathVariable("processorName")
        processorName: String
    ) : ResponseEntity<String> {
        val trackingEventProcessor = epc.eventProcessor(
            processorName,
            TrackingEventProcessor::class.java
        );

        return if(trackingEventProcessor.isPresent) {
            val eventProcessor = trackingEventProcessor.get()

            eventProcessor.shutDown()
            eventProcessor.resetTokens()
            eventProcessor.start()

            ResponseEntity.ok()
                .body("多分初期化できてるよ見てみて")
        } else {
            ResponseEntity.badRequest()
                .body("そんなプロセッサー無いよ")
        }
    }
}