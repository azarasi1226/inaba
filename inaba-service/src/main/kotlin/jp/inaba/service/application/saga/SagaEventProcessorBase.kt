package jp.inaba.service.application.saga

import org.axonframework.config.EventProcessingConfigurer
import org.axonframework.eventhandling.PropagatingErrorHandler
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration
import org.springframework.beans.factory.annotation.Autowired

/**
 * Saga用EventProcessor
 */
abstract class SagaEventProcessorBase {
    abstract val processorName: String
    open val processorCount: Int
        get() = 2

    @Autowired
    fun config(epc: EventProcessingConfigurer) {
        epc
            .registerTrackingEventProcessorConfiguration(processorName) {
                TrackingEventProcessorConfiguration
                    .forParallelProcessing(processorCount)
            }
            // Sagaは強い整合性をかけたいので、失敗したら無限リトライさせる。
            // 基本的にSagaStepを使っていれば補償トランザクションを実装していると思うから、Sagaがスタックしても問題ないという判定。
            .registerListenerInvocationErrorHandler(processorName) { PropagatingErrorHandler.INSTANCE }
    }
}
