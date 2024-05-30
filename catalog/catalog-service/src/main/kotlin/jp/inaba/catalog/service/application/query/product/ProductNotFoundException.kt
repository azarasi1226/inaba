package jp.inaba.catalog.service.application.query.product

import jp.inaba.catalog.api.domain.product.ProductId

class ProductNotFoundException(productId: ProductId) :
    Exception("productId:{$productId}を持つ商品は存在しません")
