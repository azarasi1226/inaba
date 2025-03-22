package jp.inaba.service.application.saga.createproduct

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.stock.StockIdFactory
import jp.inaba.message.product.command.DeleteProductCommand
import jp.inaba.message.product.command.ProductCreatedEvent
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
import org.springframework.beans.factory.annotation.Qualifier

private val logger = KotlinLogging.logger {}

@Saga
@ProcessingGroup(CreateProductSagaEventProcessor.PROCESSOR_NAME)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class CreateProductSaga : SagaBase() {
    @Autowired
    @Qualifier("exponentialBackoff")
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

        createStockStep.handle(createStockCommand) {
            val deleteProductCommand = DeleteProductCommand(ProductId(event.id))

            deleteProductStep.handle(deleteProductCommand) {
                fatalError()
            }
        }
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
