package jp.inaba.core.domain.common

class IdempotencyId : ULIDBaseId {
    constructor() : super()
    constructor(value: String) : super(value)
}