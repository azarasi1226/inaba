package jp.inaba.core.domain.user

import jp.inaba.core.domain.common.ULIDBaseId

class UserId : ULIDBaseId {
    constructor() : super()
    constructor(value: String) : super(value)
}