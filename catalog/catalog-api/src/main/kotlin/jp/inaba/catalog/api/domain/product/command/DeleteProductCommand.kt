package jp.inaba.catalog.api.domain.product.command

import jp.inaba.catalog.share.domain.product.ProductId

data class DeleteProductCommand(
    override val id: ProductId,
) : ProductAggregateCommand
