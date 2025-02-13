package jp.inaba.service.application.saga.createuser

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.core.domain.basket.BasketIdFactory
import jp.inaba.core.domain.user.UserId
import jp.inaba.message.basket.command.CreateBasketCommand
import jp.inaba.message.basket.event.BasketCreatedEvent
import jp.inaba.message.user.command.DeleteUserCommand
import jp.inaba.message.user.event.UserCreatedEvent
import jp.inaba.message.user.event.UserDeletedEvent
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
@ProcessingGroup(CreateUserSagaEventProcessor.PROCESSOR_NAME)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class CreateUserSaga : SagaBase() {
    @Autowired
    @JsonIgnore
    private lateinit var commandGateway: CommandGateway

    @Autowired
    @JsonIgnore
    private lateinit var basketIdFactory: BasketIdFactory

    @delegate:JsonIgnore
    private val createBasketStep by lazy { SagaStep(commandGateway, CreateBasketCommand::class) }

    @delegate:JsonIgnore
    private val deleteUserStep by lazy { SagaStep(commandGateway, DeleteUserCommand::class) }

    @StartSaga
    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId",
    )
    fun on(event: UserCreatedEvent) {
        logger.debug { "saga: [${this::class.simpleName}]開始" }

        val basketId = basketIdFactory.handle()
        val createBasketCommand = CreateBasketCommand(
            id = basketId,
            userId = UserId(event.id)
        )

        createBasketStep.handle(createBasketCommand) {
            val deleteUserCommand = DeleteUserCommand(UserId(event.id))

            deleteUserStep.handle(deleteUserCommand) {
                fatalError()
            }
        }
    }

    @EndSaga
    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId",
    )
    fun on(event: BasketCreatedEvent) {
        logger.debug { "saga: [${this::class.simpleName}]正常終了" }
    }

    @EndSaga
    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId",
    )
    fun on(event: UserDeletedEvent) {
        logger.info { "saga: [${this::class.simpleName}]補償トランザクション終了" }
    }
}
