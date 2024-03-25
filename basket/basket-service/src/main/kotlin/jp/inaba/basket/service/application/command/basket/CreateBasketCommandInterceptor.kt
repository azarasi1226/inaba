package jp.inaba.basket.service.application.command.basket

import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.service.application.command.basket.create.UserDuplicatedException
import jp.inaba.basket.service.infrastructure.jpa.basket.BasketJpaRepository
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.messaging.MessageDispatchInterceptor
import org.springframework.stereotype.Component
import java.util.function.BiFunction

@Component
class CreateBasketCommandInterceptor(
    private val basketJpaRepository: BasketJpaRepository
) : MessageDispatchInterceptor<CommandMessage<*>> {
    override fun handle(messages: MutableList<out CommandMessage<*>>): BiFunction<Int, CommandMessage<*>, CommandMessage<*>> {
        return BiFunction { _, message ->
            if(BasketCommands.Create::class.java == message.payloadType) {
                val command = message.payload as BasketCommands.Create
                setBasedConsistencyValidation(command)
            }

            message
        }
    }

    private fun setBasedConsistencyValidation(command: BasketCommands.Create) {
        if(basketJpaRepository.existsByUserId(command.userId)) {
            throw UserDuplicatedException(command.userId)
        }
    }
}