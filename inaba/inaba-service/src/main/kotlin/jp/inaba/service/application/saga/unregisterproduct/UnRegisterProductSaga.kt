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
import jp.inaba.service.infrastructure.jpa.lookupstock.LookupStockJpaRepository
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
 @ProcessingGroup(UnRegisterProductSagaEventProcessor.PROCESSOR_NAME)
 @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
 class UnRegisterProductSaga : SagaBase() {
    @Autowired
    @JsonIgnore
    private lateinit var commandGateway: CommandGateway

    @Autowired
    @JsonIgnore
    private lateinit var lookupStockJpaRepository: LookupStockJpaRepository

    @delegate:JsonIgnore
    private val deleteStockStep by lazy { SagaStep(commandGateway, DeleteStockCommand::class) }

    //TODO(これSagaの起動イベント専用にしたほうがええわ。　補償トランザクション中に起動してしまう。)
    @StartSaga
    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId",
    )
    fun on(event: ProductDeletedEvent) {
        logger.debug { "saga: [${this::class.simpleName}]開始" }

        //TODO(ここ失敗したときの対処法を考える。　このままだとここでスタックする可能性がある。)
        val entity = lookupStockJpaRepository.findByProductId(event.id).orElseThrow()
        val stockId = entity.id


        val deleteStockCommand =
            DeleteStockCommand(
                id = StockId(stockId),
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
