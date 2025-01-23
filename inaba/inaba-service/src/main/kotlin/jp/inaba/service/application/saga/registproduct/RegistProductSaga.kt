package jp.inaba.service.application.saga.registproduct

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.stock.StockId
import jp.inaba.message.product.command.DeleteProductCommand
import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.message.product.event.ProductDeletedEvent
import jp.inaba.message.stock.command.CreateStockCommand
import jp.inaba.message.stock.event.StockCreatedEvent
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.MetaDataAssociationResolver
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.SagaLifecycle
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.springframework.beans.factory.annotation.Autowired

private val logger = KotlinLogging.logger {}

@Saga
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class RegistProductSaga {
    @Autowired
    @JsonIgnore
    private lateinit var commandGateway: CommandGateway

    @delegate:JsonIgnore
    private val createStockStep by lazy { CreateStockStep(commandGateway) }

    @delegate:JsonIgnore
    private val deleteProductStep by lazy { DeleteProductStep(commandGateway) }

    @StartSaga
    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId",
    )
    fun on(event: ProductCreatedEvent) {
        val createStockCommand =
            CreateStockCommand(
                id = StockId(),
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
        logger.info { "RegistProductSaga正常終了" }
    }

    @EndSaga
    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId",
    )
    fun on(event: ProductDeletedEvent) {
        logger.warn { "RegistProductSaga補償終了" }
    }

    private fun fatalError() {
        logger.error { "致命的なエラーが発生したため補償もできませんでした。データの整合性を確認してください。" }
        SagaLifecycle.end()
    }
}
