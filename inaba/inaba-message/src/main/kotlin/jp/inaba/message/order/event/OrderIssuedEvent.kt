package jp.inaba.message.order.event

data class OrderIssuedEvent(
    override val id: String,
    val userId: String,
    val basketItems: List<BasketItem>,
) : OrderEvent {
    data class BasketItem(
        val productId: String,
        val productQuantity: Int,
    )
}
