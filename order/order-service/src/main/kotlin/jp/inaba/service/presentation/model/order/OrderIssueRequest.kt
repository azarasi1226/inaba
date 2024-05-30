package jp.inaba.service.presentation.model.order

data class OrderIssueRequest(
    val userId: String,
    val productId: String,
)
