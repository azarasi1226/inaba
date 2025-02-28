package jp.inaba.core.domain.order

import jp.inaba.core.domain.common.ULIDIdBase

class OrderId : ULIDIdBase {
    constructor() : super()
    constructor(value: String) : super(value)
}
