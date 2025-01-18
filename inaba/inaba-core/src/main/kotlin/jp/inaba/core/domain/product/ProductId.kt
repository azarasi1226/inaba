package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.ULIDBaseId

class ProductId : ULIDBaseId {
    constructor() : super()
    constructor(value: String) : super(value)
}