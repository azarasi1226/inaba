package jp.inaba.catalog.service.application.query.product

class ProductNotFoundException(productId: String)
    : Exception("productId:${productId}は存在しません")