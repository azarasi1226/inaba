package jp.inaba.service.application.projector

import org.axonframework.common.jpa.EntityManagerProvider
import org.axonframework.common.transaction.TransactionManager
import org.axonframework.config.EventProcessingConfigurer
import org.axonframework.eventhandling.EventMessage
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration
import org.axonframework.eventhandling.deadletter.jpa.JpaSequencedDeadLetterQueue
import org.springframework.beans.factory.annotation.Autowired

/**
 * 一般的なDB射影処理に使用
 */
abstract class ProjectorEventProcessorBase {
    abstract val processorName: String
    open val processorCount: Int
        get() = 2

    @Autowired
    fun config(epc: EventProcessingConfigurer) {
        epc.registerTrackingEventProcessorConfiguration(processorName) {
            TrackingEventProcessorConfiguration
                .forParallelProcessing(processorCount)
        }
            // DBの情報は基本的に強い整合性を保ちたいので失敗したら後でちゃんとリトライできるようにしたい。
            // でもリトライできるようにするために無限リトライモードにすると、イベントプロセッサーが修正するまでスタックする...
            // そんな時はデッドレターキュー！
            .registerDeadLetterQueue(processorName) {
                JpaSequencedDeadLetterQueue.builder<EventMessage<*>>()
                    .processingGroup(processorName)
                    .entityManagerProvider(it.getComponent(EntityManagerProvider::class.java))
                    .transactionManager(it.getComponent((TransactionManager::class.java)))
                    .serializer(it.serializer())
                    .build()
            }
    }
}
