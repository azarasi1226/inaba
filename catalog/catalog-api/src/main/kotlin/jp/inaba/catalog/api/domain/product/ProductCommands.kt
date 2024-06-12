package jp.inaba.catalog.api.domain.product

import org.axonframework.modelling.command.TargetAggregateIdentifier

sealed interface ProductAggregateCommand {
    @get:TargetAggregateIdentifier
    val id: ProductId
}

data class CreateProductCommand(
    override val id: ProductId,
    val name: ProductName,
    val description: ProductDescription,
    val imageUrl: ProductImageURL?,
    val price: ProductPrice,
    val quantity: ProductQuantity,
) : ProductAggregateCommand

data class UpdateProductCommand(
    override val id: ProductId,
    val name: ProductName,
    val description: ProductDescription,
    val imageUrl: ProductImageURL?,
    val price: ProductPrice,
) :ProductAggregateCommand

data class ShipmentProductCommand(
    override val id: ProductId,
    val quantity: ProductQuantity
) : ProductAggregateCommand

data class InboundProductCommand(
    override val id: ProductId,
    val quantity: ProductQuantity
) : ProductAggregateCommand

data class DeleteProductCommand(
    override val id: ProductId,
) : ProductAggregateCommand
