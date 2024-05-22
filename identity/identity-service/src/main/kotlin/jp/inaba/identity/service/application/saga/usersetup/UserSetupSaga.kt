package jp.inaba.identity.service.application.saga.usersetup

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.api.domain.basket.BasketEvents
import jp.inaba.identity.api.domain.external.auth.AuthCommands
import jp.inaba.identity.api.domain.external.auth.AuthEvents
import jp.inaba.identity.api.domain.user.UserCommands
import jp.inaba.identity.api.domain.user.UserEvents
import jp.inaba.identity.api.domain.user.UserIdFactory
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.saga.*
import org.axonframework.spring.stereotype.Saga
import org.springframework.beans.factory.annotation.Autowired

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
    private val updateIdTokenAttributeStep by lazy { UpdateIdTokenAttributeStep(commandGateway) }
    @delegate:JsonIgnore
    private val createBasketStep by lazy { CreateBasketStep(commandGateway) }
    @delegate:JsonIgnore
    private val deleteUserStep by lazy { DeleteUserStep(commandGateway) }
    @delegate:JsonIgnore
    private val deleteAuthUserStep by lazy { DeleteAuthUserStep(commandGateway) }
    @JsonIgnore
    private val logger = KotlinLogging.logger {}

    private lateinit var sagaState: UserSetupSagaState

    @StartSaga
    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId"
    )
    fun on(event: AuthEvents.SignupConfirmed) {
        sagaState = UserSetupSagaState.create(event)

        val userId = userIdFactory.handle()
        val userCreateCommand = UserCommands.Create(userId)

        createUserStep.handle(
            command = userCreateCommand,
            onFail = {
                val deleteAuthUserCommand = AuthCommands.DeleteAuthUser(sagaState.emailAddress)

                deleteAuthUserStep.handle(
                    command = deleteAuthUserCommand,
                    onFail = {
                        fatalError()
                    }
                )
            }
        )
    }

    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId"
    )
    fun on(event: UserEvents.Created) {
        sagaState = sagaState.associateUserCreatedEvent(event)

        val attribute = "custom:user_id" to event.id
        val command = AuthCommands.UpdateIdTokenAttribute(
            emailAddress = sagaState.emailAddress,
            idTokenAttributes = mapOf(attribute)
        )

        updateIdTokenAttributeStep.handle(
            command = command,
            onFail = {
                val deleteUserCommand = UserCommands.Delete(sagaState.userId!!)

                deleteUserStep.handle(
                    command = deleteUserCommand,
                    onFail = {
                        fatalError()
                    }
                )
            }
        )
    }

    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId"
    )
    fun on(event: AuthEvents.IdTokenAttributeUpdated) {
        val createBasketCommand = BasketCommands.Create(sagaState.userId!!)

        createBasketStep.handle(
            command = createBasketCommand,
            onFail = {
                val deleteUserCommand = UserCommands.Delete(sagaState.userId!!)
                deleteUserStep.handle(
                    command = deleteUserCommand,
                    onFail = {
                        fatalError()
                    }
                )
            }
        )
    }

    @EndSaga
    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId"
    )
    fun on(event: BasketEvents.Created) {
        logger.info { "UserSetupSaga正常終了 email:[${sagaState.emailAddress}]" }
    }

    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId"
    )
    fun on(event: UserEvents.Deleted) {
        val deleteAuthUserCommand = AuthCommands.DeleteAuthUser(sagaState.emailAddress)

        deleteAuthUserStep.handle(
            command = deleteAuthUserCommand,
            onFail = {
                fatalError()
            }
        )
    }

    @EndSaga
    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId"
    )
    fun on(event: AuthEvents.AuthUserDeleted) {
        logger.warn { "UserSetupSaga補償終了 email:[${sagaState.emailAddress}]" }
    }

    private fun fatalError(){
        logger.error { "UserSetupSaga強制終了 email:[${sagaState.emailAddress}]" }
        SagaLifecycle.end()
    }
}