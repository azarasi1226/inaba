package jp.inaba.core.domain.basket

import jp.inaba.core.domain.common.ULIDIdBase

class BasketId : ULIDIdBase {
    constructor() : super()
    constructor(value: String) : super(value)
}
