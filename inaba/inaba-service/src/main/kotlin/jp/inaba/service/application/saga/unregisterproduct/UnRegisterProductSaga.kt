package jp.inaba.service.application.saga.unregisterproduct

import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

//@Saga
//@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
//class UnRegisterProductSaga : SagaBase() {
//    @Autowired
//    @JsonIgnore
//    private lateinit var commandGateway: CommandGateway
//
//    @delegate:JsonIgnore
//    private val deleteStockStep by lazy { SagaStep(commandGateway, DeleteStockCommand::class) }
//
//    //TODO(これSagaの起動イベント専用にしたほうがええわ。　補償トランザクション中に起動してしまう。)
//    @StartSaga
//    @SagaEventHandler(
//        associationResolver = MetaDataAssociationResolver::class,
//        associationProperty = "traceId",An
//    )
//    fun on(event: ProductDeletedEvent) {
//        logger.debug { "saga: [${this::class.simpleName}]開始" }
//
//        val deleteStockCommand =
//            DeleteStockCommand(
//                id = StockId(TODO("どうやってStockIDとるか考えてや！！今中でRepositoryで検索かけたろうかな！")),
//            )
//
//        deleteStockStep.handle(
//            command = deleteStockCommand,
//            onFail = {
//                fatalError()
//            },
//        )
//    }
//
//    @EndSaga
//    @SagaEventHandler(
//        associationResolver = MetaDataAssociationResolver::class,
//        associationProperty = "traceId",
//    )
//    fun on(event: StockDeletedEvent) {
//        logger.debug { "saga: [${this::class.simpleName}]正常終了" }
//    }
//}