package jp.inaba.service.application.saga.registerproduct

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.stock.StockIdFactory
import jp.inaba.message.product.command.DeleteProductCommand
import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.message.product.event.ProductDeletedEvent
import jp.inaba.message.stock.command.CreateStockCommand
import jp.inaba.message.stock.event.StockCreatedEvent
import jp.inaba.service.application.saga.SagaBase
import jp.inaba.service.application.saga.SagaStep
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.config.ProcessingGroup
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.MetaDataAssociationResolver
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.springframework.beans.factory.annotation.Autowired

private val logger = KotlinLogging.logger {}

@Saga
@ProcessingGroup(RegisterProductSagaEventProcessor.PROCESSOR_NAME)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class RegisterProductSaga : SagaBase() {
    @Autowired
    @JsonIgnore
    private lateinit var commandGateway: CommandGateway

    @Autowired
    @JsonIgnore
    private lateinit var stockIdFactory: StockIdFactory

    @delegate:JsonIgnore
    private val createStockStep by lazy { SagaStep(commandGateway, CreateStockCommand::class) }

    @delegate:JsonIgnore
    private val deleteProductStep by lazy { SagaStep(commandGateway, DeleteProductCommand::class) }

    @StartSaga
    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId",
    )
    fun on(event: ProductCreatedEvent) {
        logger.debug { "saga: [${this::class.simpleName}]開始" }

        val createStockCommand =
            CreateStockCommand(
                id = stockIdFactory.handle(),
                productId = ProductId(event.id),
            )

        createStockStep.handle(
            command = createStockCommand,
            onFail = {
                val deleteProductCommand = DeleteProductCommand(ProductId(event.id))

                deleteProductStep.handle(
                    command = deleteProductCommand,
                    onFail = {
                        fatalError()
                    },
                )
            },
        )
    }

    @EndSaga
    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId",
    )
    fun on(event: StockCreatedEvent) {
        logger.debug { "saga: [${this::class.simpleName}]正常終了" }
    }

    @EndSaga
    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId",
    )
    fun on(event: ProductDeletedEvent) {
        logger.info { "saga: [${this::class.simpleName}]補償トランザクション終了" }
    }
}
