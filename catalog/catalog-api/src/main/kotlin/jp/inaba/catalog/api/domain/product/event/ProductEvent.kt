package jp.inaba.catalog.api.domain.product.event

sealed interface ProductEvent {
    val id: String
}