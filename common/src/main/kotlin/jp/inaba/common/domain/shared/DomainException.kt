package jp.inaba.common.domain.shared

class DomainException(
    val errorMessage: String,
    val errorCode: String? = null,
) : Exception("errorMessage[${errorMessage}], errorCode[${errorCode}]")