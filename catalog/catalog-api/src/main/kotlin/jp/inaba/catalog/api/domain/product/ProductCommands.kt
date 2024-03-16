package jp.inaba.catalog.api.domain.product

import org.axonframework.modelling.command.TargetAggregateIdentifier

sealed interface ProductCommand {
    @get:TargetAggregateIdentifier
    val id: ProductId
}

object ProductCommands {
    data class Create(
        override val id: ProductId,
        val name: String,
        val description: String,
        val imageUrl: String,
        val price: Int,
        val quantity: Int
    ) : ProductCommand

    data class Update(
        override val id: ProductId,
        val name: String,
        val description: String,
        val imageUrl: String,
        val price: Int,
        val quantity: Int
    ) : ProductCommand

    data class Delete(
        override val id: ProductId
    ) : ProductCommand
}