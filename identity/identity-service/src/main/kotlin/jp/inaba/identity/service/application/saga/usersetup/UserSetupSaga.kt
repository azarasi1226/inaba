package jp.inaba.identity.service.application.saga.usersetup

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.basket.api.domain.basket.command.CreateBasketCommand
import jp.inaba.basket.api.domain.basket.command.DeleteBasketCommand
import jp.inaba.basket.api.domain.basket.event.BasketCreatedEvent
import jp.inaba.basket.api.domain.basket.event.BasketDeletedEvent
import jp.inaba.identity.api.domain.external.auth.command.DeleteAuthUserCommand
import jp.inaba.identity.api.domain.external.auth.command.UpdateIdTokenAttributeForBasketIdCommand
import jp.inaba.identity.api.domain.external.auth.command.UpdateIdTokenAttributeForUserIdCommand
import jp.inaba.identity.api.domain.external.auth.event.AuthUserDeletedEvent
import jp.inaba.identity.api.domain.external.auth.event.IdTokenAttributeForBasketIdUpdatedEvent
import jp.inaba.identity.api.domain.external.auth.event.IdTokenAttributeForUserIdUpdatedEvent
import jp.inaba.identity.api.domain.external.auth.event.SignupConfirmedEvent
import jp.inaba.identity.api.domain.user.command.CreateUserCommand
import jp.inaba.identity.api.domain.user.command.DeleteUserCommand
import jp.inaba.identity.api.domain.user.event.UserCreatedEvent
import jp.inaba.identity.api.domain.user.event.UserDeletedEvent
import jp.inaba.identity.service.domain.auth.IdTokenAttributeName
import jp.inaba.identity.share.domain.user.UserIdFactory
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.MetaDataAssociationResolver
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.SagaLifecycle
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.springframework.beans.factory.annotation.Autowired
import jp.inaba.basket.share.domain.basket.BasketId

private val logger = KotlinLogging.logger {}

@Saga
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class UserSetupSaga {
    @Autowired
    @JsonIgnore
    private lateinit var commandGateway: CommandGateway

    @Autowired
    @JsonIgnore
    private lateinit var userIdFactory: UserIdFactory

    @delegate:JsonIgnore
    private val createUserStep by lazy { CreateUserStep(commandGateway) }

    @delegate:JsonIgnore
    private val updateIdTokenAttributeForUserIdStep by lazy { UpdateIdTokenAttributeForUserIdStep(commandGateway) }

    @delegate:JsonIgnore
    private val createBasketStep by lazy { CreateBasketStep(commandGateway) }

    @delegate:JsonIgnore
    private val updateIdTokenAttributeForBasketIdStep by lazy { UpdateIdTokenAttributeForBasketIdStep(commandGateway) }

    @delegate:JsonIgnore
    private val deleteBasketStep by lazy { DeleteBasketStep(commandGateway) }

    @delegate:JsonIgnore
    private val deleteUserStep by lazy { DeleteUserStep(commandGateway) }

    @delegate:JsonIgnore
    private val deleteAuthUserStep by lazy { DeleteAuthUserStep(commandGateway) }

    private lateinit var sagaState: UserSetupSagaState

    @StartSaga
    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId",
    )
    fun on(event: SignupConfirmedEvent) {
        sagaState = UserSetupSagaState(event)
        logger.info { "UserSetupSaga開始 email:[${sagaState.emailAddress}]" }

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

        val command =
            UpdateIdTokenAttributeForUserIdCommand(
                emailAddress = sagaState.emailAddress,
                userId = sagaState.userId,
            )

        updateIdTokenAttributeForUserIdStep.handle(
            command = command,
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
    fun on(event: IdTokenAttributeForUserIdUpdatedEvent) {
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

        val command =
            UpdateIdTokenAttributeForBasketIdCommand(
                emailAddress = sagaState.emailAddress,
                basketId = sagaState.basketId,
            )

        updateIdTokenAttributeForBasketIdStep.handle(
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
    fun on(event: IdTokenAttributeForBasketIdUpdatedEvent) {
        logger.info { "UserSetupSaga正常終了 email:[${sagaState.emailAddress}]" }
    }

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
        logger.warn { "UserSetupSaga補償終了 email:[${sagaState.emailAddress}]" }
    }

    private fun fatalError() {
        logger.error { "UserSetupSaga強制終了 email:[${sagaState.emailAddress}]" }
        logger.error { "保障トランザクションが最後まで実行されませんでした。データの整合性を確認してください。" }
        SagaLifecycle.end()
    }
}
