package jp.inaba.basket.service.application.command.basket.setbasketitem

import jp.inaba.catalog.api.domain.product.ProductId

class ProductNotFoundException(productId: ProductId)
    : Exception("productId:[${productId.value}]が見つかりませんでした。")