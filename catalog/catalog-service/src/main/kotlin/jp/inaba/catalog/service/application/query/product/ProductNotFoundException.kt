package jp.inaba.catalog.service.application.query.product

import jp.inaba.catalog.api.domain.product.ProductId

class ProductNotFoundException(productId: String)
    : Exception("productId:${productId}は存在しません")