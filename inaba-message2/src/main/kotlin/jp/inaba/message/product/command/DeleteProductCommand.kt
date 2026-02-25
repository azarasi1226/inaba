package jp.inaba.message.product.command

import jp.inaba.core.domain.product.ProductId

data class DeleteProductCommand(
    override val id: ProductId,
) : ProductAggregateCommand
