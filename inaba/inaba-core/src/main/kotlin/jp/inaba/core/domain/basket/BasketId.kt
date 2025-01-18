package jp.inaba.core.domain.basket

import jp.inaba.core.domain.common.ULIDBaseId

class BasketId : ULIDBaseId {
    constructor() : super()
    constructor(value: String) : super(value)
}

