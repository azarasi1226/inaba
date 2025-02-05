package jp.inaba.service.application.saga.unregisterproduct

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.core.domain.stock.StockId
import jp.inaba.message.product.event.ProductDeletedEvent
import jp.inaba.message.stock.command.DeleteStockCommand
import jp.inaba.message.stock.event.StockDeletedEvent
import jp.inaba.service.application.saga.SagaBase
import jp.inaba.service.application.saga.SagaStep
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.MetaDataAssociationResolver
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.springframework.beans.factory.annotation.Autowired

private val logger = KotlinLogging.logger {}

@Saga
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class UnRegisterProductSaga : SagaBase() {
    @Autowired
    @JsonIgnore
    private lateinit var commandGateway: CommandGateway

    @delegate:JsonIgnore
    private val deleteStockStep by lazy { SagaStep(commandGateway, DeleteStockCommand::class) }

    @StartSaga
    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId",
    )
    fun on(event: ProductDeletedEvent) {
        logger.debug { "saga: [${this::class.simpleName}]開始" }

        val deleteStockCommand =
            DeleteStockCommand(
                id = StockId(TODO("どうやってStockIDとるか考えてや！！今中でRepositoryで検索かけたろうかな！")),
            )

        deleteStockStep.handle(
            command = deleteStockCommand,
            onFail = {
                fatalError()
            },
        )
    }

    @EndSaga
    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId",
    )
    fun on(event: StockDeletedEvent) {
        logger.debug { "saga: [${this::class.simpleName}]正常終了" }
    }
}