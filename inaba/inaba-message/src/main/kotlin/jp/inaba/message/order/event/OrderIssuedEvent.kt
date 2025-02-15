package jp.inaba.message.order.event

data class OrderIssuedEvent(
    val id: String,
    val userId: String,
    val basketItems: List<BasketItem>,
) {
    data class BasketItem(
        val productId: String,
        val stockQuantity: Int,
    )
}
