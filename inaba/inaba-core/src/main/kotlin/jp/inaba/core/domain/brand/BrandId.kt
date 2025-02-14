package jp.inaba.core.domain.brand

import jp.inaba.core.domain.common.ULIDIdBase

class BrandId : ULIDIdBase {
    constructor() : super()
    constructor(value: String) : super(value)
}