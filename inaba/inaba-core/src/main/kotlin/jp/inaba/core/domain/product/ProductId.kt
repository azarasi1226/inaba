package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.ULIDIdBase

class ProductId : ULIDIdBase {
    constructor() : super()
    constructor(value: String) : super(value)
}