package jp.inaba.basket.service.configuration

import jp.inaba.basket.service.application.command.basket.SetBasketItemCommandInterceptor
import org.axonframework.commandhandling.CommandBus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
class AxonConfiguration {
    @Autowired
    fun commandBusConfiguration(
        commandBus: CommandBus,
        setBasketItemCommandInterceptor: SetBasketItemCommandInterceptor
    ) {
        commandBus.registerDispatchInterceptor(setBasketItemCommandInterceptor)
    }
}