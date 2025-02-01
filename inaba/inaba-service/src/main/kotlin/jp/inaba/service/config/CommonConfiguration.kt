package jp.inaba.service.config

import jp.inaba.core.domain.stock.StockIdFactory
import jp.inaba.core.domain.stock.StockIdFactoryImpl
import jp.inaba.core.domain.user.UserIdFactory
import jp.inaba.core.domain.user.UserIdFactoryImpl
import jp.inaba.service.config.interceptor.ExceptionWrappingCommandHandlerInterceptor
import jp.inaba.service.config.interceptor.LoggingCommandDispatchInterceptor
import jp.inaba.service.utlis.isWrapUseCaseError
import org.axonframework.commandhandling.CommandBus
import org.axonframework.commandhandling.CommandExecutionException
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.commandhandling.gateway.DefaultCommandGateway
import org.axonframework.commandhandling.gateway.ExponentialBackOffIntervalRetryScheduler
import org.axonframework.commandhandling.gateway.RetryScheduler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.Executors


@Configuration
class CommonConfiguration {
    @Bean
    fun userIdFactory(): UserIdFactory {
        return UserIdFactoryImpl()
    }

    @Bean
    fun stockIdFactory(): StockIdFactory {
        return StockIdFactoryImpl()
    }

    @Autowired
    fun commandBus(commandBus: CommandBus) {
        commandBus.registerHandlerInterceptor(ExceptionWrappingCommandHandlerInterceptor())
        commandBus.registerDispatchInterceptor(LoggingCommandDispatchInterceptor())
    }

    @Bean
    fun commandGateway(commandBus: CommandBus): CommandGateway {
        val scheduledExecutorService = Executors.newScheduledThreadPool(5)
        val retryScheduler: RetryScheduler =
            ExponentialBackOffIntervalRetryScheduler
                .builder()
                .retryExecutor(scheduledExecutorService)
                .maxRetryCount(3)
                .backoffFactor(1000)
                .addNonTransientFailurePredicate {
                    // AxonServerを使用した場合、CommandHandlerの例外はすべてCommandExecutionExceptionにラップされる。
                    // その中でもUseCaseExceptionから発生したエラーだった場合はビジネス例外なのでRetryさせない。
                    if (it is CommandExecutionException) {
                        return@addNonTransientFailurePredicate it.isWrapUseCaseError()
                    }

                    return@addNonTransientFailurePredicate false
                }
                .build()

        return DefaultCommandGateway
            .builder()
            .commandBus(commandBus)
            .retryScheduler(retryScheduler)
            .build()
    }
}
