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
class RegisterProductSaga {
    @Autowired
    @JsonIgnore
    private lateinit var commandGateway: CommandGateway

    @Autowired
    @JsonIgnore
    private lateinit var stockIdFactory: StockIdFactory

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
        logger.debug { "RegisterProductSaga開始" }

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
        logger.debug { "RegisterProductSaga正常終了" }
    }

    @EndSaga
    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId",
    )
    fun on(event: ProductDeletedEvent) {
        logger.info { "RegisterProductSaga補償終了" }
    }

    private fun fatalError() {
        logger.error { "RegisterProductSagaにて致命的なエラーが発生しました。データの整合性を確認してください。" }
        SagaLifecycle.end()
    }
}
