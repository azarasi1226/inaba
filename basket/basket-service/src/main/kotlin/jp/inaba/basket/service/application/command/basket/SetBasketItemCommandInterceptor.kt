package jp.inaba.basket.service.application.command.basket

import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.service.application.command.basket.setbasketitem.BasketNotFoundException
import jp.inaba.basket.service.application.command.basket.setbasketitem.ProductNotFoundException
import jp.inaba.basket.service.infrastructure.jpa.basket.BasketJpaRepository
import jp.inaba.basket.service.infrastructure.jpa.product.ProductJpaRepository
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.messaging.MessageDispatchInterceptor
import org.springframework.stereotype.Component
import java.util.function.BiFunction

@Component
class SetBasketItemCommandInterceptor(
    private val basketJpaRepository: BasketJpaRepository,
    private val productJpaRepository: ProductJpaRepository
) : MessageDispatchInterceptor<CommandMessage<*>> {
    override fun handle(messages: MutableList<out CommandMessage<*>>): BiFunction<Int, CommandMessage<*>, CommandMessage<*>> {
        return BiFunction { _, message ->
            if(BasketCommands.SetBasketItem::class.java == message.payloadType) {
                val command = message.payload as BasketCommands.SetBasketItem
                setBasedConsistencyValidation(command)
            }

            message
        }
    }

    private fun setBasedConsistencyValidation(command: BasketCommands.SetBasketItem) {
        basketJpaRepository.findById(command.id.value)
            .orElseThrow { BasketNotFoundException(command.id) }

        // 商品の存在チェック
        productJpaRepository.findById(command.productId.value)
            .orElseThrow{ ProductNotFoundException(command.productId) }
    }
}