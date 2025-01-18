package jp.inaba.core.domain.order

import jp.inaba.core.domain.common.ULIDBaseId

class OrderId : ULIDBaseId {
    constructor() : super()
    constructor(value: String) : super(value)
}