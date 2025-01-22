package jp.inaba.core.domain.common

class IdempotencyId : ULIDIdBase {
    constructor() : super()
    constructor(value: String) : super(value)
}