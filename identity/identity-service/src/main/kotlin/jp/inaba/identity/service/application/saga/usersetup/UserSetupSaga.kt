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
import jp.inaba.identity.api.domain.user.UserId
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

    @JsonIgnore
    private val createUserStep = CreateUserStep(commandGateway)
    @JsonIgnore
    private val updateIdTokenAttributeStep = UpdateIdTokenAttributeStep(commandGateway)
    @JsonIgnore
    private val createBasketStep = CreateBasketStep(commandGateway)
    @JsonIgnore
    private val deleteAuthUserStep = DeleteAuthUserStep(commandGateway)
    @JsonIgnore
    private val logger = KotlinLogging.logger {}

    private lateinit var sagaState: UserSetupSagaState

    @StartSaga
    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId"
    )
    fun on(event: AuthEvents.SignupConfirmed) {
        logger.info { "UserSetupSaga開始" }
        sagaState = UserSetupSagaState.create(event)

        val userId = UserId()
        val userCreateCommand = UserCommands.Create(userId)

        createUserStep
            .onFail {
                logger.error { "ユーザーの作成に失敗しました。 exception:[${it}]" }

                val deleteAuthUserCommand = AuthCommands.DeleteAuthUser(sagaState.emailAddress)
                deleteAuthUserStep
                    .onFail {
                        logger.error { "認証ユーザーの削除に失敗しました。 exception:[${it}]" }

                        fatalError()
                    }
                    .execute(deleteAuthUserCommand)
            }
            .execute(userCreateCommand)
    }

    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId"
    )
    fun on(event: UserEvents.Created) {
        val userId = UserId(event.id)

        updateIdTokenAttributeStep
            .onFail {
                logger.error { "IdTokenAttributeの更新に失敗しました。 exception: [$it]" }
            }
    }

    @SagaEventHandler(
        associationResolver = MetaDataAssociationResolver::class,
        associationProperty = "traceId"
    )
    fun on(event: AuthEvents.IdTokenAttributeUpdated) {
        val command = BasketCommands.Create(sagaState.userId!!)

        createBasketStep
    }

    @EndSaga
    fun on(event: BasketEvents.Created) {
        logger.info { "UserSetupSaga: Basket created" }
    }

    private fun fatalError(){
        logger.error { "致命的なエラーが発生しました。Sagaを強制停止します" }
        SagaLifecycle.end()
    }
}