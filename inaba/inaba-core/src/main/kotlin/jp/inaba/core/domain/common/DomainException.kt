package jp.inaba.core.domain.common

class DomainException(
    val errorCode: String,
    val errorMessage: String,
) : Exception("errorMessage[$errorMessage], errorCode[$errorCode]") {
    constructor(code: String) : this(code, "")
}
