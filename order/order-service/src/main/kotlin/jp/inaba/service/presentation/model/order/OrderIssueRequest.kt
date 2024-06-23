package jp.inaba.service.presentation.model.order

data class OrderIssueRequest(
    val userId: String,
    val productId: String,
    val basketItems: List<BasketItem>,
) {
    data class BasketItem(
        val productId: String,
        val productQuantity: Int,
    )
}
