package jp.inaba.service.config

import jp.inaba.service.config.interceptor.ExceptionWrappingCommandHandlerInterceptor
import jp.inaba.service.config.interceptor.ExceptionWrappingQueryHandlerInterceptor
import jp.inaba.service.config.interceptor.LoggingCommandDispatchInterceptor
import jp.inaba.service.config.interceptor.LoggingQueryDispatchInterceptor
import jp.inaba.service.utlis.isWrapUseCaseError
import org.axonframework.commandhandling.CommandBus
import org.axonframework.commandhandling.CommandExecutionException
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.commandhandling.gateway.DefaultCommandGateway
import org.axonframework.commandhandling.gateway.ExponentialBackOffIntervalRetryScheduler
import org.axonframework.commandhandling.gateway.RetryScheduler
import org.axonframework.queryhandling.QueryBus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.Executors

@Configuration
class AxonConfiguration {
    @Autowired
    fun commandBus(commandBus: CommandBus) {
        commandBus.registerHandlerInterceptor(ExceptionWrappingCommandHandlerInterceptor())
        commandBus.registerDispatchInterceptor(LoggingCommandDispatchInterceptor())
    }

    @Autowired
    fun queryBus(queryBus: QueryBus) {
        queryBus.registerHandlerInterceptor(ExceptionWrappingQueryHandlerInterceptor())
        queryBus.registerDispatchInterceptor(LoggingQueryDispatchInterceptor())
    }

    @Bean
    fun commandGateway(commandBus: CommandBus): CommandGateway {
        return DefaultCommandGateway.builder()
            .commandBus(commandBus)
            .build()
    }

    @Bean(name = ["exponentialBackoff"])
    fun exponentialBackOffCommandGateway(commandBus: CommandBus): CommandGateway {
        val scheduledExecutorService = Executors.newScheduledThreadPool(5)
        val retryScheduler: RetryScheduler =
            ExponentialBackOffIntervalRetryScheduler
                .builder()
                .retryExecutor(scheduledExecutorService)
                // 失敗時、1s, 2s, 4s...みたいな感じでリトライする。
                .maxRetryCount(3)
                .backoffFactor(1000)
                // AxonServerを使用した場合、CommandHandler内で起きた例外はすべてCommandExecutionExceptionにラップされる。
                // そのままだとすべての例外がRuntimeExceptionであり、ビジネス例外でもリトライが走ってしまうため、以下で制御する。
                .addNonTransientFailurePredicate {
                    if (it is CommandExecutionException) {
                        // UseCase例外の場合はリトライしても成功しないのでリトライ停止
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
