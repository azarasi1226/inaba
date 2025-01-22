package jp.inaba.core.domain.stock

import jp.inaba.core.domain.common.ULIDIdBase

class StockId : ULIDIdBase {
    constructor() : super()
    constructor(value: String) : super(value)
}