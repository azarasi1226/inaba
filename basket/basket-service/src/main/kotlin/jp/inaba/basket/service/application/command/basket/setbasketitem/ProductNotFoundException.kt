package jp.inaba.basket.service.application.command.basket.setbasketitem

import jp.inaba.catalog.api.domain.product.ProductId

class ProductNotFoundException(productId: ProductId)
    : Exception("productId:[${productId.value}]を持つ商品が見つかりませんでした。")