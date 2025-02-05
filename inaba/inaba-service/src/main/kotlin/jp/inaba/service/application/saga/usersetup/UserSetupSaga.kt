package jp.inaba.service.application.saga.usersetup

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.user.UserIdFactory
import jp.inaba.message.auth.command.DeleteAuthUserCommand
import jp.inaba.message.auth.event.AuthUserDeletedEvent
import jp.inaba.message.auth.event.SignupConfirmedEvent
import jp.inaba.message.auth.query.GetAuthUserQuery
import jp.inaba.message.auth.query.GetAuthUserResult
import jp.inaba.message.basket.command.CreateBasketCommand
import jp.inaba.message.basket.command.DeleteBasketCommand
import jp.inaba.message.basket.event.BasketCreatedEvent
import jp.inaba.message.basket.event.BasketDeletedEvent
import jp.inaba.message.user.command.CreateUserCommand
import jp.inaba.message.user.command.DeleteUserCommand
import jp.inaba.message.user.command.LinkBasketIdCommand
import jp.inaba.message.user.command.LinkSubjectCommand
import jp.inaba.message.user.event.BasketIdLinkedEvent
import jp.inaba.message.user.event.SubjectLinkedEvent
import jp.inaba.message.user.event.UserCreatedEvent
import jp.inaba.message.user.event.UserDeletedEvent
import jp.inaba.service.application.saga.SagaBase
import jp.inaba.service.application.saga.SagaStep
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.extensions.kotlin.query
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.MetaDataAssociationResolver
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.spring.stereotype.Saga
import org.springframework.beans.factory.annotation.Autowired

private val logger = KotlinLogging.logger {}

@Saga
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class UserSetupSaga : SagaBase() {
    @Autowired
    @JsonIgnore
    private lateinit var commandGateway: CommandGateway

    @Autowired
    @JsonIgnore
    private lateinit var queryGateway: QueryGateway

    @Autowired
    @JsonIgnore
    private lateinit var userIdFactory: UserIdFactory

    @delegate:JsonIgnore
    private val createUserStep by lazy { SagaStep(commandGateway, CreateUserCommand::class) }

    @delegate:JsonIgnore
    private val createBasketStep by lazy { SagaStep(commandGateway, CreateBasketCommand::class) }

    @delegate:JsonIgnore
    private val linkSubjectStep by lazy { SagaStep(commandGateway, LinkSubjectCommand::class) }

    @delegate:JsonIgnore
    private val linkBasketIdStep by lazy { SagaStep(commandGateway, LinkBasketIdCommand::class) }

    @delegate:JsonIgnore
    private val deleteBasketStep by lazy { SagaStep(commandGateway, DeleteBasketCommand::class) }

    @delegate:JsonIgnore
    private val deleteUserStep by lazy { SagaStep(commandGateway, DeleteUserCommand::class) }

    @delegate:JsonIgnore
    private val deleteAuthUserStep by lazy { SagaStep(commandGateway, DeleteAuthUserCommand::class) }

    private lateinit var sagaState: UserSetupSagaState

    @StartSaga
    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId",
    )
    fun on(event: SignupConfirmedEvent) {
        logger.debug { "saga: [${this::class.simpleName}]開始" }

        sagaState = UserSetupSagaState(event)

        val userId = userIdFactory.handle()
        val createUserCommand = CreateUserCommand(userId)

        createUserStep.handle(
            command = createUserCommand,
            onFail = {
                val deleteAuthUserCommand = DeleteAuthUserCommand(sagaState.emailAddress)

                deleteAuthUserStep.handle(
                    command = deleteAuthUserCommand,
                    onFail = {
                        fatalError()
                    },
                )
            },
        )
    }

    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId",
    )
    fun on(event: UserCreatedEvent) {
        sagaState.associateUserCreatedEvent(event)

        val basketId = BasketId()
        val createBasketCommand =
            CreateBasketCommand(
                id = basketId,
                userId = sagaState.userId,
            )

        createBasketStep.handle(
            command = createBasketCommand,
            onFail = {
                val deleteUserCommand = DeleteUserCommand(sagaState.userId)
                deleteUserStep.handle(
                    command = deleteUserCommand,
                    onFail = {
                        fatalError()
                    },
                )
            },
        )
    }

    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId",
    )
    fun on(event: BasketCreatedEvent) {
        sagaState.associateBasketCreatedEvent(event)

        // EmailAddressからSubjectを取得
        val query = GetAuthUserQuery(emailAddress = sagaState.emailAddress)
        val result = queryGateway.query<GetAuthUserResult, GetAuthUserQuery>(query).get()

        val command =
            LinkSubjectCommand(
                id = sagaState.userId,
                subject = result.subject
            )

        linkSubjectStep.handle(
            command = command,
            onFail = {
                val deleteBasketCommand = DeleteBasketCommand(sagaState.basketId)

                deleteBasketStep.handle(
                    command = deleteBasketCommand,
                    onFail = {
                        fatalError()
                    },
                )
            },
        )
    }

    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId",
    )
    fun on(event: SubjectLinkedEvent) {
        val command =
            LinkBasketIdCommand(
                id = sagaState.userId,
                basketId = sagaState.basketId
            )

        linkBasketIdStep.handle(
            command = command,
            onFail = {
                val deleteBasketCommand = DeleteBasketCommand(sagaState.basketId)

                deleteBasketStep.handle(
                    command = deleteBasketCommand,
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
    fun on(event: BasketIdLinkedEvent) {
        logger.debug { "saga: [${this::class.simpleName}]正常終了" }
    }


    // -------------------------------------------------------------------
    // -----------------------↓補償フェーズ↓--------------------------------
    // -------------------------------------------------------------------
    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId",
    )
    fun on(event: BasketDeletedEvent) {
        val deleteUserCommand = DeleteUserCommand(sagaState.userId)

        deleteUserStep.handle(
            command = deleteUserCommand,
            onFail = {
                fatalError()
            },
        )
    }

    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId",
    )
    fun on(event: UserDeletedEvent) {
        val deleteAuthUserCommand = DeleteAuthUserCommand(sagaState.emailAddress)

        deleteAuthUserStep.handle(
            command = deleteAuthUserCommand,
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
    fun on(event: AuthUserDeletedEvent) {
        logger.info { "saga: [${this::class.simpleName}]補償トランザクション終了" }
    }
}
