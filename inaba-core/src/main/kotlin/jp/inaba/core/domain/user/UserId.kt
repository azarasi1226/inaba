package jp.inaba.core.domain.user

import jp.inaba.core.domain.common.ULIDIdBase

class UserId : ULIDIdBase {
    constructor() : super()
    constructor(value: String) : super(value)
}
