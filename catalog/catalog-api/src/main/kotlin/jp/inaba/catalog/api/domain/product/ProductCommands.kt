package jp.inaba.catalog.api.domain.product

import org.axonframework.modelling.command.TargetAggregateIdentifier

sealed interface ProductCommand {
    @get:TargetAggregateIdentifier
    val id: ProductId
}

data class CreateProductCommand(
    override val id: ProductId,
    val productName: String,
    val description: String,
    val imageUrl: String,
    val price: Int,
    val quantity: Int
) : ProductCommand

data class UpdateProductCommand(
    override val id: ProductId,
    val productName: String,
    val description: String,
    val imageUrl: String,
    val price: Int,
    val quantity: Int
) : ProductCommand

data class DeleteProductCommand(
    override val id: ProductId
) : ProductCommand