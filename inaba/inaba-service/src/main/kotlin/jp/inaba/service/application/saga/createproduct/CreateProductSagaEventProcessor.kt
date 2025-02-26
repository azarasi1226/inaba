package jp.inaba.service.application.saga.createproduct

import jp.inaba.service.application.saga.SagaEventProcessorBase
import org.springframework.context.annotation.Configuration

@Configuration
class CreateProductSagaEventProcessor : SagaEventProcessorBase() {
    companion object {
        const val PROCESSOR_NAME = "create-product-saga"
    }

    override val processorName: String
        get() = PROCESSOR_NAME
}
