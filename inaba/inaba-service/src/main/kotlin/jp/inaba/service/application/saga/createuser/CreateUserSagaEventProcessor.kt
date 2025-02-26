package jp.inaba.service.application.saga.createuser

import jp.inaba.service.application.saga.SagaEventProcessorBase
import org.springframework.context.annotation.Configuration

@Configuration
class CreateUserSagaEventProcessor : SagaEventProcessorBase() {
    companion object {
        const val PROCESSOR_NAME = "create-user-saga"
    }

    override val processorName: String
        get() = PROCESSOR_NAME
}
