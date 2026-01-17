package jp.inaba.service.domain.order

enum class OrderStatus(
    val value: Int,
) {
    Issued(1),
    Completed(2),
    Failed(3),
}
