package jp.inaba.basket.service.configuration.inmemory

import jp.inaba.catalog.api.domain.product.ProductEvents
import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.catalog.api.domain.product.ProductPrice
import org.axonframework.eventhandling.gateway.EventGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("app-inmemory")
class InitializePublishEvent {
    @Autowired
    fun publishEvent(eventGateway: EventGateway) {
        val events = listOf<Any>(
            ProductEvents.Created(
                id = ProductId().value,
                name = "りんご",
                description = "青森県産",
                imageUrl = "ringo.png",
                price = ProductPrice(69).value,
                quantity = 100
            ),
            ProductEvents.Created(
                id = ProductId().value,
                name = "バナナ",
                description = "フィリピン産",
                imageUrl = "banana.png",
                price = ProductPrice(200).value,
                quantity = 100
            ),
            ProductEvents.Created(
                id = ProductId().value,
                name = "ninjaマウス",
                description = "50g",
                imageUrl = "mause.png",
                price = ProductPrice(9000).value,
                quantity = 100
            ),
            ProductEvents.Created(
                id = ProductId().value,
                name = "ninjaキーボード",
                description = "薄型",
                imageUrl = "keybord.png",
                price = ProductPrice(23999).value,
                quantity = 100
            )
        )


            events.forEach {
                eventGateway.publish(it)
            }
    }
}