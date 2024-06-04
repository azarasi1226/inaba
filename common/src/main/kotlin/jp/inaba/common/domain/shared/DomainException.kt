package jp.inaba.common.domain.shared

class DomainException(
    val errorCode: String,
    val errorMessage: String,
) : Exception("errorMessage[$errorMessage], errorCode[$errorCode]") {
    constructor(code: String) : this(code, "")
}
