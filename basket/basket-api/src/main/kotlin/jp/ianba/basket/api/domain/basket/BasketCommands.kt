package jp.ianba.basket.api.domain.basket

import org.axonframework.modelling.command.TargetAggregateIdentifier

sealed interface BasketCommand {
    @get:TargetAggregateIdentifier
    val id: BasketId
}

data class CreateBasketCommand(
    override val id: BasketId,
    //TODO(後でUserIDの値オブジェクトにする)
    val userId: String
) : BasketCommand

data class SetItemCommand(
    override val id: BasketId,
    val itemId: String,
    val itemQuantity: ItemQuantity
) : BasketCommand

data class DeleteItemCommand(
    override val id: BasketId,
    val itemId: String
) : BasketCommand

data class ClearBasketCommand(
    override val id: BasketId
) : BasketCommand