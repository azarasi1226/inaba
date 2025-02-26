package jp.inaba.service.application.projector

import org.axonframework.config.EventProcessingConfigurer
import org.axonframework.eventhandling.PropagatingErrorHandler
import org.springframework.beans.factory.annotation.Autowired

/**
 * 集約間をまたいだ検証や、強い整合性を保ちたい場合に使用
 */
abstract class LookupProjectorEventProcessorBase {
    abstract val processorName: String

    @Autowired
    fun config(epc: EventProcessingConfigurer) {
        epc.registerSubscribingEventProcessor(processorName)
            // LookupTableはDB操作に失敗したらEventの発行もなかったことにしたいレベルの強い整合性をかけたいときに使う。
            // PropagatingErrorHandlerを利用することで、EventをpublishしたスレッドにExceptionが伝播される。
            .registerListenerInvocationErrorHandler(processorName) { PropagatingErrorHandler.INSTANCE }
    }
}